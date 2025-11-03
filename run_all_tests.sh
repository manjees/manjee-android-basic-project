#!/bin/bash

# Manjee Android Basic Project - Complete Test Suite Runner
# This script runs all available tests in the project

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Print colored output
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Header
echo "============================================"
echo "   Manjee Android Test Suite Runner"
echo "============================================"
echo ""

# Check if gradlew exists and is executable
if [ ! -f "./gradlew" ]; then
    print_error "gradlew not found!"
    exit 1
fi

if [ ! -x "./gradlew" ]; then
    print_info "Making gradlew executable..."
    chmod +x ./gradlew
fi

# Clean previous build artifacts (optional)
if [ "$1" == "--clean" ]; then
    print_info "Cleaning build artifacts..."
    ./gradlew clean
    print_success "Clean completed"
    echo ""
fi

# Track test results
FAILED_TESTS=()
PASSED_TESTS=()

# Function to run tests and track results
run_test() {
    local test_name=$1
    local test_command=$2

    print_info "Running $test_name..."

    if ./gradlew $test_command; then
        print_success "$test_name passed!"
        PASSED_TESTS+=("$test_name")
    else
        print_error "$test_name failed!"
        FAILED_TESTS+=("$test_name")
        # Don't exit immediately, continue with other tests
    fi
    echo ""
}

# Start time
START_TIME=$(date +%s)

echo "============================================"
echo "   Running Unit Tests"
echo "============================================"
echo ""

# Run domain module unit tests
run_test "Domain Module Unit Tests" ":domain:test"

# Run data module unit tests
run_test "Data Module Unit Tests" ":data:test"

# Run app module unit tests
run_test "App Module Unit Tests" ":app:test"

# Run all unit tests together (as a verification)
run_test "All Unit Tests (Combined)" "test"

echo "============================================"
echo "   Running Android Instrumented Tests"
echo "============================================"
echo ""

# Check if emulator or device is connected
if adb devices | grep -q "device$"; then
    print_info "Android device/emulator detected"

    # Run instrumented tests
    run_test "Data Module Instrumented Tests" ":data:connectedAndroidTest"
    run_test "App Module Instrumented Tests" ":app:connectedAndroidTest"
    run_test "All Instrumented Tests (Combined)" "connectedAndroidTest"
else
    print_warning "No Android device/emulator connected. Skipping instrumented tests."
    print_info "To run instrumented tests, please connect a device or start an emulator."
fi

echo "============================================"
echo "   Running Static Analysis"
echo "============================================"
echo ""

# Run lint checks
run_test "Lint Checks" "lint"

# Run ktlint if configured
if grep -q "ktlint" build.gradle.kts 2>/dev/null || grep -q "ktlint" app/build.gradle.kts 2>/dev/null; then
    run_test "Kotlin Lint" "ktlintCheck"
fi

# End time
END_TIME=$(date +%s)
DURATION=$((END_TIME - START_TIME))

echo "============================================"
echo "   Test Results Summary"
echo "============================================"
echo ""

# Print summary
print_info "Total time: ${DURATION} seconds"
echo ""

if [ ${#PASSED_TESTS[@]} -gt 0 ]; then
    print_success "Passed tests (${#PASSED_TESTS[@]}):"
    for test in "${PASSED_TESTS[@]}"; do
        echo "  ✓ $test"
    done
    echo ""
fi

if [ ${#FAILED_TESTS[@]} -gt 0 ]; then
    print_error "Failed tests (${#FAILED_TESTS[@]}):"
    for test in "${FAILED_TESTS[@]}"; do
        echo "  ✗ $test"
    done
    echo ""

    print_error "Some tests failed! Please check the logs above for details."
    exit 1
else
    print_success "All tests passed successfully!"
fi

# Generate test reports location info
echo ""
echo "============================================"
echo "   Test Reports Location"
echo "============================================"
echo ""
print_info "Unit test reports:"
echo "  • app/build/reports/tests/testDebugUnitTest/index.html"
echo "  • data/build/reports/tests/test/index.html"
echo "  • domain/build/reports/tests/test/index.html"
echo ""
print_info "Instrumented test reports:"
echo "  • app/build/reports/androidTests/connected/index.html"
echo "  • data/build/reports/androidTests/connected/index.html"
echo ""
print_info "Lint reports:"
echo "  • app/build/reports/lint-results-debug.html"

exit 0