package com.pierreduchemin.smsforward.data

import com.pierreduchemin.smsforward.data.source.database.ReplacementRule
import com.pierreduchemin.smsforward.data.source.database.ReplacementRuleDao

class ReplacementRuleRepository(private val replacementRuleDao: ReplacementRuleDao) : ReplacementRuleDataSource {

    override fun observeReplacementRules() =
        replacementRuleDao.observeReplacementRules()

    override fun getReplacementRules() =
        replacementRuleDao.getReplacementRules()

    override fun insertReplacementRule(replacementRule: ReplacementRule) {
        replacementRuleDao.insertReplacementRule(replacementRule)
    }

    override fun deleteReplacementRule(replacementRule: ReplacementRule) {
        replacementRuleDao.deleteReplacementRule(replacementRule)
    }

    override fun deleteReplacementRuleById(id: Long) {
        replacementRuleDao.deleteReplacementRuleById(id)
    }
}
