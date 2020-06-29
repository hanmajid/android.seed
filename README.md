# android.seed

## About

🚀 *Still in development* 🚀

A social media prototype that demonstrates the usage of Android Jetpack libraries and recommended app architecture and design.

## Features

Please refer to Screenshots page (TODO) to see these features in action.

* Onboarding page for first-time user
* Login page
* Register page
* TBD

## Libraries

Please refer to Screenshots page (TODO) to see these libraries in action.

1. Databinding
   1. ✅ Binding adapters.
   2. ✅ Two-way databinding using LiveData: For login and registration form.
2. Navigation
   1. ✅ Navigate using Safe Args Gradle plugin for type-safety.
   2. ✅ Design with different form factor: Use `DrawerLayout` or `BottomNavigationView` depending on available screen size.
   3. ✅ Nested graphs using `<include />` tag: Separate authentication flow into different navigation graph.
   4. ✅ Conditional navigation: Navigate to Onboarding page for first-time user and navigate to Login page when user is unauthenticated.
   5. ✅ Explicit (via app widget) and implicit deep links (via URL) for Chat page.
   6. ❌ Transition animation.
   7. ❌ Navigation testing.
   8. ❌ Dynamic feature module.
3. ViewPager2
4. Testing
5. ViewModel
6. Material Design
7. Paging3
   1. ✅ Load paged data from single source (network)
   2. ❌ Load paged data from multiple source (network & database)
   2. ❌ Display the loading state
   3. ❌ Add item separators



## Version

1.0
