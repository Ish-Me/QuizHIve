# QuizHive 

A modern Android quiz application that fetches real-time multiple-choice questions from the OpenTDB REST API. 
Built with clean architecture and industry-standard tools used in professional Android development.

---

##  Tech Stack

| Category | Technology |
| Language | Kotlin |
| UI Framework | Jetpack Compose |
| Architecture | MVVM + Clean Architecture |
| Dependency Injection | Hilt |
| Networking | Retrofit + OkHttp |
| JSON Parsing | Gson |
| Local Database | Room |
| Async Operations | Coroutines + StateFlow |
| Navigation | Jetpack Navigation Compose |
| Preferences | DataStore |

---

##  API

This app uses the **[Open Trivia Database](https://opentdb.com/)** — a free, open source trivia question database.
```
Base URL: https://opentdb.com/
Endpoint: GET /api.php?amount=10&category={id}&difficulty={level}&type=multiple
```

No API key required.

---

##  Getting Started

### Prerequisites
- Android Studio Hedgehog or newer
- Android SDK 24+
- Kotlin 1.9.23

### Installation

1. Clone the repository
```bash
git clone https://github.com/Ish-Me/QuizHive.git
```

2. Open the project in Android Studio

3. Let Gradle sync and download dependencies

4. Run the app on an emulator or physical device

---

##  Key Implementation Details

**Sealed UiState** — The ViewModel uses a sealed class to represent all possible UI states (`Idle`, `Loading`, `Active`, `Result`, `Error`), making state management predictable and exhaustive.

**Repository Pattern** — The UI never touches the network directly. All data flows through the repository interface, making the code testable and the data source swappable.

**HTML Decoding** — OpenTDB returns HTML-encoded text like `&amp;` and `&#039;`. The repository layer decodes all text using `Html.fromHtml()` before passing it to the UI.

**Coroutine Timer** — Each question has a 15-second timer implemented with `viewModelScope.launch` and `delay()`, automatically selecting an empty answer when time runs out.

---


##  Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

-
