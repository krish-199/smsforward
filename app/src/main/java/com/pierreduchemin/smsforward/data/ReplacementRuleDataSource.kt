package com.pierreduchemin.smsforward.data

import androidx.lifecycle.LiveData
import com.pierreduchemin.smsforward.data.source.database.ReplacementRule

interface ReplacementRuleDataSource {
    fun observeReplacementRules(): LiveData<List<ReplacementRule>>
    fun getReplacementRules(): List<ReplacementRule>
    fun insertReplacementRule(replacementRule: ReplacementRule)
    fun deleteReplacementRule(replacementRule: ReplacementRule)
    fun deleteReplacementRuleById(id: Long)
}
