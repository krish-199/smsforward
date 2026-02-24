package com.pierreduchemin.smsforward.utils

import android.content.Context
import android.util.Log
import com.pierreduchemin.smsforward.R
import com.pierreduchemin.smsforward.data.ForwardModelRepository
import com.pierreduchemin.smsforward.data.GlobalModelRepository
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException
import javax.inject.Inject

class RedirectionManager @Inject constructor(
    private val globalModelRepository: GlobalModelRepository,
    private val forwardModelRepository: ForwardModelRepository
) {

    companion object {
        private val TAG by lazy { RedirectionManager::class.java.simpleName }
    }

    fun onSmsReceived(context: Context, phoneNumberFrom: String, message: String) {
        if (!isRedirectionActivated()) {
            Log.i(TAG, "Redirection not activated")
            return
        }

        val forwardModels = forwardModelRepository.getForwardModels()
        if (forwardModels.isEmpty()) {
            Log.i(TAG, "Nothing to redirect")
            return
        }

        Log.d(TAG, "SMS received from $phoneNumberFrom")
        forwardModels.forEach { dbForwardModel ->
            val matchesSource = if (dbForwardModel.isRegex) {
                try {
                    Pattern.compile(dbForwardModel.from).matcher(phoneNumberFrom).matches()
                } catch (e: PatternSyntaxException) {
                    Log.e(
                        TAG,
                        "Invalid pattern. This should not happen as regex are supposed to be already validated."
                    )
                    false
                }
            } else {
                dbForwardModel.from == phoneNumberFrom
            }

            if (!matchesSource) return@forEach

            var redirectedMessage = message
            if (!dbForwardModel.contentRegex.isNullOrBlank()) {
                try {
                    val pattern = Pattern.compile(dbForwardModel.contentRegex!!)
                    val matcher = pattern.matcher(message)
                    if (matcher.find()) {
                        if (!dbForwardModel.template.isNullOrBlank()) {
                            val template = dbForwardModel.template!!
                            val templateRegex = Regex("\\{(\\d+)\\}")
                            redirectedMessage = templateRegex.replace(template) { result ->
                                val groupIndex = result.groupValues[1].toInt()
                                if (groupIndex <= matcher.groupCount()) {
                                    matcher.group(groupIndex) ?: ""
                                } else {
                                    result.value
                                }
                            }
                        }
                    } else {
                        Log.d(TAG, "Message does not match content regex, skipping")
                        return@forEach
                    }
                } catch (e: PatternSyntaxException) {
                    Log.e(TAG, "Invalid content regex: ${dbForwardModel.contentRegex}")
                    return@forEach
                }
            }

            Log.d(TAG, "Caught a SMS from $phoneNumberFrom that matches ${dbForwardModel.from}")
            var source = phoneNumberFrom
            if (dbForwardModel.vfromName.isNotBlank()) {
                source = dbForwardModel.vfromName + " | " + phoneNumberFrom
            }

            sendSMS(
                context, dbForwardModel.to, context.getString(
                    R.string.notification_info_sms_received_from,
                    source,
                    redirectedMessage
                )
            )
        }
    }

    fun isRedirectionActivated(): Boolean {
        val globalModel = globalModelRepository.getGlobalModel()
        if (globalModel == null) {
            Log.d(TAG, "globalModel is null")
            return true
        }
        if (!globalModel.activated) {
            Log.i(TAG, "Redirection not activated")
            return false
        }
        return true
    }

    private fun sendSMS(context: Context, phoneNumber: String, message: String) {
        Log.d(TAG, "Sending SMS to $phoneNumber: $message")
        val smsManager = SdkUtils.getSmsManager(context)
        val messageDivided = smsManager.divideMessage(message)

        if (messageDivided.size == 1) {
            Log.d(TAG, "Sending as single message")
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        } else {
            Log.d(TAG, "Sending as multipart message")
            smsManager.sendMultipartTextMessage(phoneNumber, null, messageDivided, null, null)
        }
    }
}