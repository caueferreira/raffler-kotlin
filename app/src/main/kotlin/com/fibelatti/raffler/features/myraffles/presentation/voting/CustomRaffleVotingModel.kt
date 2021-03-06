package com.fibelatti.raffler.features.myraffles.presentation.voting

import com.fibelatti.raffler.core.functional.Mapper
import com.fibelatti.raffler.features.myraffles.CustomRaffleVoting
import com.fibelatti.raffler.features.myraffles.presentation.common.CustomRaffleModel
import javax.inject.Inject

data class CustomRaffleVotingModel(
    val customRaffleId: Long,
    val description: String,
    val pin: Int,
    val totalVotes: Int,
    val votes: Map<String, Int>
) {
    val mostVoted: Map<String, Int> = votes.filter { it.value == votes.values.max() }
}

class CustomRaffleVotingModelMapper @Inject constructor() : Mapper<CustomRaffleVoting, CustomRaffleVotingModel> {
    override fun map(param: CustomRaffleVoting): CustomRaffleVotingModel {
        return with(param) {
            CustomRaffleVotingModel(customRaffleId, description, pin, totalVotes, votes)
        }
    }

    override fun mapReverse(param: CustomRaffleVotingModel): CustomRaffleVoting {
        return with(param) {
            CustomRaffleVoting(customRaffleId, description, pin, totalVotes, votes)
        }
    }

    fun map(pin: Int, customRaffle: CustomRaffleModel): CustomRaffleVotingModel = with(customRaffle) {
        CustomRaffleVotingModel(
            customRaffleId = id,
            description = description,
            pin = pin,
            totalVotes = 0,
            votes = includedItems.map { it.description to 0 }.toMap()
        )
    }
}
