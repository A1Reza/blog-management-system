"use strict";

// =========================
// API Configuration
// =========================

const API_BASE_URL = "api";

const AUTH_URL = `${API_BASE_URL}/auth`;
const POSTS_URL = `${API_BASE_URL}/posts`;

// =========================
// DOM Elements
// =========================

const loginForm = document.getElementById("login-form");
const registerForm = document.getElementById("register-form");
const createPostForm = document.getElementById("create-post-form");

const showLoginBtn = document.getElementById("show-login-btn");
const showRegisterBtn = document.getElementById("show-register-btn");
const logoutBtn = document.getElementById("logout-btn");
const refreshPostsBtn = document.getElementById("refresh-posts-btn");

const authMessage = document.getElementById("auth-message");
const postsMessage = document.getElementById("posts-message");
const userInfo = document.getElementById("user-info");

const createPostSection = document.getElementById("create-post-section");
const postsContainer = document.getElementById("posts-container");

const postDetailsSection = document.getElementById("post-details-section");
const postDetails = document.getElementById("post-details");
const backToPostsBtn = document.getElementById("back-to-posts-btn");

// =========================
// Application State
// =========================

let currentUsername = null;
let currentPostId = null;

// =========================
// API Helper
// =========================

async function request(url, options = {}) {
    const response = await fetch(url, {
        ...options,
        credentials: "same-origin",
        headers: {
            Accept: "application/json",
            ...options.headers
        }
    });

    const responseText = await response.text();

    let data = null;

    if (responseText) {
        try {
            data = JSON.parse(responseText);
        } catch {
            data = responseText;
        }
    }

    if (!response.ok) {
        const message =
            typeof data === "object" && data !== null
                ? data.message || data.error
                : data;

        throw new Error(
            message || `Request failed with status ${response.status}`
        );
    }

    return data;
}

function jsonOptions(method, body) {
    return {
        method,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    };
}

// =========================
// Message Helpers
// =========================

function showMessage(element, message, type = "") {
    element.textContent = message;
    element.classList.remove("message-success", "message-error");

    if (type === "success") {
        element.classList.add("message-success");
    } else if (type === "error") {
        element.classList.add("message-error");
    }
}

function getErrorMessage(error) {
    return error.message || "An unexpected error occurred.";
}

// =========================
// Authentication UI
// =========================

function showAuthForm(formToShow) {
    loginForm.hidden = formToShow !== loginForm;
    registerForm.hidden = formToShow !== registerForm;

    showMessage(authMessage, "");

    document.getElementById("auth").scrollIntoView({
        behavior: "smooth",
        block: "start"
    });
}

function updateAuthenticationUI() {
    const loggedIn = currentUsername !== null;

    showLoginBtn.hidden = loggedIn;
    showRegisterBtn.hidden = loggedIn;
    logoutBtn.hidden = !loggedIn;

    loginForm.hidden = true;
    registerForm.hidden = true;

    userInfo.hidden = !loggedIn;
    createPostSection.hidden = !loggedIn;

    if (loggedIn) {
        userInfo.textContent = `Logged in as ${currentUsername}`;
    } else {
        userInfo.textContent = "";
    }

    renderPosts();
}

// =========================
// Register
// =========================

registerForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const formData = new FormData(registerForm);

    const user = {
        username: formData.get("username").trim(),
        email: formData.get("email").trim(),
        password: formData.get("password")
    };

    try {
        await request(
            `${AUTH_URL}/register`,
            jsonOptions("POST", user)
        );

        registerForm.reset();

        showMessage(
            authMessage,
            "Registration successful. Please log in.",
            "success"
        );

        loginForm.hidden = false;
        registerForm.hidden = true;

        document.getElementById("login-username").value = user.username;
    } catch (error) {
        showMessage(authMessage, getErrorMessage(error), "error");
    }
});

// =========================
// Login
// =========================

loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const formData = new FormData(loginForm);

    const credentials = {
        username: formData.get("username").trim(),
        password: formData.get("password")
    };

    try {
        await request(
            `${AUTH_URL}/login`,
            jsonOptions("POST", credentials)
        );

        currentUsername = credentials.username;

        loginForm.reset();

        updateAuthenticationUI();

        showMessage(authMessage, "Login successful.", "success");

        await loadPosts();
    } catch (error) {
        showMessage(authMessage, getErrorMessage(error), "error");
    }
});

// =========================
// Logout
// =========================

logoutBtn.addEventListener("click", async () => {
    try {
        await request(`${AUTH_URL}/logout`, {
            method: "POST"
        });

        currentUsername = null;
        currentPostId = null;

        postDetailsSection.hidden = true;

        updateAuthenticationUI();

        showMessage(authMessage, "You have logged out.", "success");

        await loadPosts();
    } catch (error) {
        showMessage(authMessage, getErrorMessage(error), "error");
    }
});

// =========================
// Authentication Buttons
// =========================

showLoginBtn.addEventListener("click", () => {
    showAuthForm(loginForm);
});

showRegisterBtn.addEventListener("click", () => {
    showAuthForm(registerForm);
});

// =========================
// Create Post
// =========================

createPostForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    if (!currentUsername) {
        showMessage(authMessage, "Please log in first.", "error");
        return;
    }

    const formData = new FormData(createPostForm);

    const post = {
        title: formData.get("title").trim(),
        content: formData.get("content").trim()
    };

    try {
        await request(
            POSTS_URL,
            jsonOptions("POST", post)
        );

        createPostForm.reset();

        showMessage(postsMessage, "Post created successfully.", "success");

        await loadPosts();
    } catch (error) {
        showMessage(postsMessage, getErrorMessage(error), "error");
    }
});

// =========================
// Load Posts
// =========================

async function loadPosts() {
    showMessage(postsMessage, "Loading posts...");
    postsContainer.replaceChildren();

    try {
        const data = await request(POSTS_URL);

        const posts = Array.isArray(data)
            ? data
            : data?.data ?? data?.posts ?? [];

        renderPosts(posts);

        if (posts.length === 0) {
            showMessage(postsMessage, "No posts found.");
        } else {
            showMessage(postsMessage, "");
        }
    } catch (error) {
        showMessage(postsMessage, getErrorMessage(error), "error");
    }
}

// =========================
// Render Posts
// =========================

function renderPosts(posts) {
    if (!Array.isArray(posts)) {
        return;
    }

    postsContainer.replaceChildren();

    posts.forEach((post) => {
        const card = document.createElement("article");
        card.className = "post-card";

        const title = document.createElement("h3");
        title.textContent = post.title ?? "Untitled";

        const content = document.createElement("p");
        const fullContent = post.content ?? "";

        content.textContent =
            fullContent.length > 180
                ? `${fullContent.slice(0, 180)}...`
                : fullContent;

        const meta = document.createElement("div");
        meta.className = "post-meta";

        const author = document.createElement("span");
        author.textContent = `By ${post.authorUsername ?? "Unknown"}`;

        const date = document.createElement("span");
        date.textContent = formatDate(post.createdAt);

        meta.append(author, date);

        const actions = document.createElement("div");
        actions.className = "post-actions";

        const detailsBtn = document.createElement("button");
        detailsBtn.type = "button";
        detailsBtn.textContent = "Read More";

        detailsBtn.addEventListener("click", () => {
            loadPostDetails(post.id);
        });

        actions.append(detailsBtn);

        const isOwner =
            currentUsername !== null &&
            post.authorUsername === currentUsername;

        if (isOwner) {
            const editBtn = document.createElement("button");
            editBtn.type = "button";
            editBtn.className = "edit-btn";
            editBtn.textContent = "Edit";

            editBtn.addEventListener("click", () => {
                editPost(post);
            });

            const deleteBtn = document.createElement("button");
            deleteBtn.type = "button";
            deleteBtn.className = "delete-btn";
            deleteBtn.textContent = "Delete";

            deleteBtn.addEventListener("click", () => {
                deletePost(post.id);
            });

            actions.append(editBtn, deleteBtn);
        }

        card.append(title, content, meta, actions);
        postsContainer.append(card);
    });
}

// =========================
// Post Details
// =========================

async function loadPostDetails(postId) {
    try {
        const post = await request(`${POSTS_URL}/${postId}`);

        currentPostId = post.id;

        postDetails.replaceChildren();

        const title = document.createElement("h1");
        title.textContent = post.title ?? "Untitled";

        const meta = document.createElement("div");
        meta.className = "post-meta";

        const author = document.createElement("span");
        author.textContent = `By ${post.authorUsername ?? "Unknown"}`;

        const date = document.createElement("span");
        date.textContent = formatDate(post.createdAt);

        meta.append(author, date);

        const content = document.createElement("p");
        content.textContent = post.content ?? "";

        postDetails.append(title, meta, content);

        document.getElementById("posts").hidden = true;
        postDetailsSection.hidden = false;

        postDetailsSection.scrollIntoView({
            behavior: "smooth",
            block: "start"
        });
    } catch (error) {
        showMessage(postsMessage, getErrorMessage(error), "error");
    }
}

backToPostsBtn.addEventListener("click", () => {
    postDetailsSection.hidden = true;
    document.getElementById("posts").hidden = false;
    currentPostId = null;
});

// =========================
// Edit Post
// =========================

async function editPost(post) {
    const newTitle = prompt("Enter the new title:", post.title);

    if (newTitle === null) {
        return;
    }

    const newContent = prompt("Enter the new content:", post.content);

    if (newContent === null) {
        return;
    }

    const updatedPost = {
        title: newTitle.trim(),
        content: newContent.trim()
    };

    try {
        await request(
            `${POSTS_URL}/${post.id}`,
            jsonOptions("PUT", updatedPost)
        );

        showMessage(postsMessage, "Post updated successfully.", "success");

        await loadPosts();

        if (currentPostId === post.id) {
            await loadPostDetails(post.id);
        }
    } catch (error) {
        showMessage(postsMessage, getErrorMessage(error), "error");
    }
}

// =========================
// Delete Post
// =========================

async function deletePost(postId) {
    const confirmed = confirm(
        "Are you sure you want to delete this post?"
    );

    if (!confirmed) {
        return;
    }

    try {
        await request(`${POSTS_URL}/${postId}`, {
            method: "DELETE"
        });

        if (currentPostId === postId) {
            currentPostId = null;
            postDetailsSection.hidden = true;
            document.getElementById("posts").hidden = false;
        }

        showMessage(postsMessage, "Post deleted successfully.", "success");

        await loadPosts();
    } catch (error) {
        showMessage(postsMessage, getErrorMessage(error), "error");
    }
}

// =========================
// Utility Functions
// =========================

function formatDate(value) {
    if (!value) {
        return "Date unavailable";
    }

    const date = new Date(value);

    if (Number.isNaN(date.getTime())) {
        return String(value);
    }

    return date.toLocaleString();
}

// =========================
// Refresh Posts
// =========================

refreshPostsBtn.addEventListener("click", loadPosts);

// =========================
// Initial Load
// =========================

updateAuthenticationUI();
loadPosts();
