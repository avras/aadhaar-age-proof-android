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
const val PUBLIC_PARAMETERS_RESET_WORK_NAME = "public_parameters_reset_work"

// Tags
const val TAG_OUTPUT = "OUTPUT"
const val TAG_RESET_PP = "RESET_PP"
const val TAG_GEN_PP = "GEN_PP"

// Keys
const val KEY_PUBLIC_PARAMS = "KEY_PUBLIC_PARAMS"

// Delay for testing
const val DELAY_TIME_MILLIS: Long = 3000