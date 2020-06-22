# The Moviedb
Simple Apps that use MovieDB API

### What i use:
- Testing: Barista (Espresso Framework)
- Coroutine
- Kotlin
- Endless Recyclerview
- etc.

### Requirement:
- Internet connection, i do not yet implement error handling when there is no connection

# Requirement for testing
1. For safety, please test this in emulator because i use instrumented testing and for
avoid the Activity never launch in real device. I use Xiaomi, maybe from the security.

2. Please disable emulator animation. I use test-butler linkedin to assert if animation disabled.
On your device, under Settings > Developer options, disable the following 3 settings:

- Window animation scale
- Transition animation scale
- Animator duration scale

res: https://developer.android.com/training/testing/espresso/setup#set-up-environment


Best regards,
Nanang Fitrianto
