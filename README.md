# Android Lock Pattern App

## Overview

The Android Lock Pattern App allows users to set a custom pattern as a form of security measure. Users can draw a pattern to unlock, and the app also checks the time taken to draw the pattern for additional security.

## Features

- **Set Pattern**: Users can create a new lock pattern.
- **Confirm Pattern**: Users must re-enter the same pattern for confirmation.
- **Unlock**: Users can attempt to unlock using the previously set pattern.
- **Suspicious Activity Detection**: The app detects if the pattern is drawn too slowly compared to the original time, flagging potential suspicious activity.

## How to Use

### Setting and Confirming a Pattern

1. **Set Pattern**: Tap on the "Set Pattern" button and draw a pattern on the screen.
2. **Confirm Pattern**: After setting a pattern, the app will prompt you to confirm it. Draw the pattern again for confirmation.

### Unlocking

1. **Enter Unlock Mode**: Tap on the "Unlock Mode" button.
2. **Draw Pattern**: Draw the previously set pattern to unlock.

### Resetting Pattern

- You can reset the drawn pattern at any time by tapping the "Reset Pattern" button.

### Suspicious Activity

- If the pattern is correct but the time taken to draw it is significantly longer than the time taken to set it (more than 50% longer), the app will flag this as suspicious.

## Important Notes

- The pattern and its timing are currently stored in memory and will reset when the app is closed or restarted.
