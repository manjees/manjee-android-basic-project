# Testing Guide - Manjee Android Basic Project

## 🚀 Quick Start

### Run All Tests Locally
```bash
# Make script executable (first time only)
chmod +x ./run_all_tests.sh

# Run all tests
./run_all_tests.sh

# Run all tests with clean build
./run_all_tests.sh --clean
```

### Run Specific Test Types
```bash
# Unit tests only
./gradlew test

# Unit tests for specific module
./gradlew :domain:test
./gradlew :data:test
./gradlew :app:test

# Instrumented tests (requires emulator/device)
./gradlew connectedAndroidTest

# Lint checks
./gradlew lint
```

## 🤖 CI/CD with GitHub Actions

### Automatic Test Execution
Tests run automatically on:
- Every push to `main` or `develop` branches
- Every pull request to `main` or `develop`
- Manual trigger via GitHub Actions UI

### Available Workflows

#### 1. **android-ci.yml** (Full Test Suite)
- Runs unit tests on multiple API levels (29, 34)
- Executes instrumented tests on Android emulator
- Performs lint checks
- Builds both Debug and Release APKs
- Generates comprehensive test reports

#### 2. **android-ci-simple.yml** (Quick Tests)
- Runs unit tests only (no emulator)
- Performs lint checks
- Builds Debug APK
- Faster execution (~5-10 minutes)
- Ideal for feature branches

### Workflow Selection
- **Use Simple CI** for: Regular commits, feature development
- **Use Full CI** for: Pre-release testing, main branch merges

## 📊 Test Coverage

### Current Test Coverage
| Module | Unit Tests | Instrumented Tests |
|--------|------------|-------------------|
| Domain | ✅ | N/A |
| Data | ✅ | ✅ |
| App | ✅ | ✅ |

### Test Files Location
```
domain/
  src/test/java/           # Domain unit tests

data/
  src/test/java/           # Data unit tests
  src/androidTest/java/    # Data instrumented tests (Room, migrations)

app/
  src/test/java/           # App unit tests (ViewModels)
  src/androidTest/java/    # App instrumented tests (UI)
```

## 🔍 Test Reports

### Local Test Reports
After running tests locally, find reports at:
```
# Unit test reports
app/build/reports/tests/testDebugUnitTest/index.html
data/build/reports/tests/test/index.html
domain/build/reports/tests/test/index.html

# Instrumented test reports
app/build/reports/androidTests/connected/index.html
data/build/reports/androidTests/connected/index.html

# Lint reports
app/build/reports/lint-results-debug.html
```

### GitHub Actions Artifacts
Test results are uploaded as artifacts:
1. Go to Actions tab in GitHub
2. Select the workflow run
3. Download artifacts from the summary page

## 🛠️ Troubleshooting

### Common Issues

#### 1. Emulator not found for instrumented tests
```bash
# Start Android emulator manually
emulator -avd <your_avd_name>

# Or skip instrumented tests
./gradlew test  # Unit tests only
```

#### 2. Out of memory errors
```bash
# Increase memory in gradle.properties
org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8
```

#### 3. Test failures in CI but not locally
- Check API level differences
- Verify time zone settings
- Review locale/language settings
- Check network permissions

#### 4. Slow test execution
```bash
# Enable parallel execution
./gradlew test --parallel --max-workers=4

# Use Gradle daemon
./gradlew test --daemon
```

## ✅ Pre-Push Checklist

Before pushing your changes:

1. **Run local tests**
   ```bash
   ./run_all_tests.sh
   ```

2. **Check lint warnings**
   ```bash
   ./gradlew lint
   ```

3. **Verify build**
   ```bash
   ./gradlew :app:assembleDebug
   ```

4. **Review test reports**
   - Open HTML reports in browser
   - Fix any failing tests
   - Address lint warnings

## 📈 Improving Test Coverage

### Adding New Tests

#### Unit Test Template
```kotlin
@RunWith(JUnit4::class)
class ExampleTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `test description`() = runTest {
        // Given

        // When

        // Then
    }
}
```

#### Instrumented Test Template
```kotlin
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun testExample() {
        // Test implementation
    }
}
```

### Test Naming Convention
- Use descriptive names with backticks
- Format: `test [condition] should [expected result]`
- Example: `test search with empty query should return empty list`

## 🎯 Performance Benchmarks

Expected test execution times:
- Unit tests: ~30 seconds
- Instrumented tests: ~3-5 minutes
- Full CI pipeline: ~10-15 minutes
- Simple CI pipeline: ~5-7 minutes

## 📚 Additional Resources

- [Android Testing Documentation](https://developer.android.com/training/testing)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [JUnit Documentation](https://junit.org/junit4/)
- [Mockk Documentation](https://mockk.io/)

## 💡 Tips

1. **Speed up local tests**: Use `--offline` flag if dependencies are cached
2. **Debug specific test**: Add `--debug` flag for detailed output
3. **Run failed tests only**: Use `--rerun-tasks` to force re-execution
4. **Test in isolation**: Use `@SmallTest`, `@MediumTest`, `@LargeTest` annotations
5. **Mock external dependencies**: Use MockWebServer for API tests

---

For questions or issues, please check the [main README](README.md) or create an issue in the repository.