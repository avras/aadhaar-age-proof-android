package aadhaar.ageproof

// Notification Channel constants

// Name of Notification Channel for verbose notifications of background work
val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
    "Verbose WorkManager Notifications"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
    "Shows notifications whenever work starts"
val NOTIFICATION_TITLE: CharSequence = "WorkRequest Starting"
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
const val NOTIFICATION_ID = 1

// Task names
const val PUBLIC_PARAMETERS_GEN_WORK_NAME = "public_parameters_gen_work"

// Keys
const val TAG_OUTPUT = "OUTPUT"
const val KEY_PUBLIC_PARAMS = "KEY_PUBLIC_PARAMS"

// Delay for testing
const val DELAY_TIME_MILLIS: Long = 3000