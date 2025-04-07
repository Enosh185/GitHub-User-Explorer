# GitHub User Explorer 🚀

A clean, minimal Android app that explores GitHub profiles and connections.

## 🛠 Built With
- **Java**
- **MVVM Architecture**
- **Room DB** (for caching)
- **Retrofit2** (for network calls)
- **Shimmer Layout** (for skeleton loading)
- **Glide** (for image loading)
- **SwipeRefreshLayout** (for pull-to-refresh)
- **GitHub REST API**

---

## 🔍 Features

### 🔎 GitHub User Search
- Enter a GitHub username
- If found, displays:
  - Avatar 🖼
  - Name 👤
  - Username 🧾
  - Bio 🧬
  - Follower & Following count
- If not found, shows a clean "User Not Found" view

### 👥 Follower & Following View
- Tap on **followers/following count** to see full lists
- Scrollable and searchable
- Tap on any user in the list to view their profile

### 🌙 Dark Mode Support
- Toggle between light/dark themes
- Auto applies to all screens

---

## 🌟 Bonus Features Implemented
- ✅ Skeleton loading screens using Shimmer
- ✅ Pull to refresh on followers/following list
- ✅ Profile caching with Room DB
- ✅ Cache invalidation using background API fetch
- ✅ Real-time search in follower/following lists
- ✅ GitHub logo as launcher icon

---

## 📦 Installation
1. Clone the repo:
```bash
git clone https://github.com/Enosh185/GitHub-User-Explorer.git

2. Open in Android Studio

3 Build and Run 🚀

⏱ Time Log
Date	Task Description	Time Spent
Apr 6, 2025	App Structure, GitHub API Integration	3 hrs
Apr 6, 2025	UI Design & Theming	3 hrs
Apr 7, 2025	Followers/Following flow + Profile Details	5.5 hrs
Apr 7, 2025	Skeleton loading, pull-to-refresh	1.5 hrs
Apr 7, 2025	Bug Fixes, Git Setup	4 hrs
Total		17 hrs

🤝 Author
Enosh Mosuganti
📧 enosh185@gmail.com
Built with caffeine, discipline, and way too many Toast.makeText() calls. ☕


