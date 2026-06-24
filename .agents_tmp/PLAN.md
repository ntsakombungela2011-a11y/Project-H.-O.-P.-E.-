# 1. OBJECTIVE

Enhance the Dark Humor Android app with five major features: clipboard copy functionality, multi-select joke categories, complete theme system with 12 color options, centralized color library, and CI/CD release automation.

# 2. CONTEXT SUMMARY

### Current Architecture
- **Framework**: Android Jetpack Compose with Material 3, Kotlin, Hilt DI
- **State Management**: DataStore for preferences, StateFlow for UI state
- **Networking**: Retrofit with Kotlinx Serialization for JokeAPI
- **Database**: Room for caching and favorites
- **CI/CD**: Basic GitHub Actions workflow (build + APK upload)
- **Current Theme**: Dark-only with hardcoded Purple/Grey/Pink colors

### Key Files to Modify
1. `app/src/main/java/com/example/darkhumor/ui/theme/Color.kt` - Theme color constants
2. `app/src/main/java/com/example/darkhumor/ui/theme/Theme.kt` - Theme management
3. `app/src/main/java/com/example/darkhumor/data/preferences/PreferenceManager.kt` - Categories + theme
4. `app/src/main/java/com/example/darkhumor/data/api/JokeApiService.kt` - Dynamic categories
5. `app/src/main/java/com/example/darkhumor/data/repository/JokeRepository.kt` - Category handling
6. `app/src/main/java/com/example/darkhumor/ui/settings/SettingsScreen.kt` - Settings UI updates
7. `app/src/main/java/com/example/darkhumor/ui/settings/SettingsViewModel.kt` - Settings logic
8. `app/src/main/java/com/example/darkhumor/ui/main/MainScreen.kt` - Add copy button
9. `app/src/main/java/com/example/darkhumor/ui/favorites/FavoritesScreen.kt` - Add copy button
10. `app/src/main/java/com/example/darkhumor/MainActivity.kt` - Theme application
11. `app/build.gradle.kts` - Release signing configuration
12. `.github/workflows/build.yml` - CI/CD release workflow

# 3. APPROACH OVERVIEW

1. **Color Library**: Define all 13 theme colors as named constants in Color.kt (12 themes + White)
2. **Theme System**: Create AppTheme enum and ThemeManager singleton, apply via CompositionLocal/State
3. **Category Expansion**: Extend PreferenceManager with category set, update API with dynamic path
4. **Clipboard Copy**: Add ContentCopyIcon button, use LocalClipboardManager, show Snackbar
5. **CI/CD Release**: Configure release signing, enhance workflow for APK/AAB generation and GitHub releases

# 4. IMPLEMENTATION STEPS

### Step 1: Theme Color Library (Feature 4)
- Update `Color.kt` with 13 theme colors as named constants
- Include all specified hex values + White

### Step 2: Theme System (Feature 3)
- Create `AppTheme.kt` enum with all 12 theme options
- Create `ThemeManager.kt` for theme persistence via DataStore
- Update `Theme.kt` to accept dynamic color scheme based on selected theme
- Update `MainActivity.kt` to apply theme from ThemeManager

### Step 3: Category Expansion (Feature 2)
- Extend `PreferenceManager.kt` with `selectedCategories: Set<String>` (default: {"Dark"})
- Update `UserPreferences` data class
- Update `JokeApiService.kt` with @Path parameter for dynamic category path
- Update `JokeRepository.kt` to build comma-separated category path
- Update `SettingsScreen.kt` with category checkboxes (6 categories)
- Update `SettingsViewModel.kt` with category functions

### Step 4: Clipboard Copy Feature (Feature 1)
- Add `copyJoke()` function in `MainScreen.kt` using LocalClipboardManager
- Add Copy icon button beside Share button in `JokeDisplay`
- Show Snackbar "Copied to clipboard" on copy
- Handle single and twopart joke formats correctly

### Step 5: Favorites Screen Enhancement
- Update `FavoritesScreen.kt` with copy button on each joke card
- Show Snackbar "Copied to clipboard" on copy

### Step 6: CI/CD Release System (Feature 5)
- Add release signing configuration in `app/build.gradle.kts`
- Update `.github/workflows/build.yml`:
  - Add release trigger on version tags
  - Generate release APK with signing
  - Generate AAB with signing
  - Create GitHub Release with tag
  - Upload only APK/AAB files (no ZIP)
  - Verify artifact generation

# 5. TESTING AND VALIDATION

### Build Verification
- `./gradlew assembleDebug` - Debug build passes
- `./gradlew assembleRelease` - Release build passes
- `./gradlew lint` - No lint errors
- `./gradlew testDebugUnitTest` - All tests pass

### Feature Validation
1. **Clipboard**: Copy button visible on joke cards, copies correct format, snackbar shows "Copied to clipboard"
2. **Categories**: Multi-select works, persists across restart, defaults to Dark if empty, API path builds correctly (e.g., /joke/Programming,Pun,Dark)
3. **Theme**: All 12 theme colors apply immediately to all UI components (app bars, cards, dialogs, snackbars, etc.), persists across restart
4. **CI/CD**: Release workflow generates APK and AAB, GitHub release created with assets, no ZIP files

### Visual Validation
- Theme changes apply immediately without restart
- Cards, dialogs, snackbars, buttons use theme colors
- System status/navigation bars update with theme
