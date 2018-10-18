package com.fibelatti.raffler.features.myraffles.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.fibelatti.raffler.core.functional.Result
import com.fibelatti.raffler.core.functional.catching
import com.fibelatti.raffler.features.myraffles.CustomRaffle
import com.fibelatti.raffler.features.myraffles.CustomRaffleItem
import com.fibelatti.raffler.features.myraffles.CustomRaffleRepository
import javax.inject.Inject

class CustomRaffleDataSource @Inject constructor(
    private val customRaffleDao: CustomRaffleDao,
    private val customRaffleItemDao: CustomRaffleItemDao,
    private val customRaffleWithItemsDtoMapper: CustomRaffleWithItemsDtoMapper,
    private val customRaffleItemDtoMapper: CustomRaffleItemDtoMapper
) : CustomRaffleRepository {
    override suspend fun getAllCustomRaffles(): Result<List<CustomRaffle>> =
        catching { customRaffleDao.getAllCustomRaffles().let(customRaffleWithItemsDtoMapper::mapList) }

    override suspend fun getCustomRaffleById(id: Long): Result<CustomRaffle> =
        catching { customRaffleDao.getCustomRaffleById(id).let(customRaffleWithItemsDtoMapper::map) }

    override suspend fun saveCustomRaffle(customRaffle: CustomRaffle): Result<CustomRaffle> {
        return catching {
            val customRaffleId: Long
            val customRaffleWithItemsDto = customRaffle.let(customRaffleWithItemsDtoMapper::mapReverse)

            customRaffleId = customRaffleDao.saveCustomRaffle(customRaffleWithItemsDto.customRaffleDto)
            val items = customRaffleWithItemsDto.items.map { it.copy(customRaffleId = customRaffleId) }

            customRaffleItemDao.deleteCustomRaffleItemsByCustomRaffleId(customRaffleId)
            customRaffleItemDao.saveCustomRaffleItems(*items.toTypedArray())

            customRaffleDao.getCustomRaffleById(customRaffleId)
                .let(customRaffleWithItemsDtoMapper::map)
        }
    }

    override suspend fun updateCustomRaffleItem(customRaffleItem: CustomRaffleItem): Result<Unit> =
        catching {
            customRaffleItemDao.saveCustomRaffleItems(customRaffleItemDtoMapper.mapReverse(customRaffleItem))
        }

    override suspend fun deleteCustomRaffleById(id: Long): Result<Unit> =
        catching { customRaffleDao.deleteCustomRaffleById(id) }
}

@Dao
interface CustomRaffleDao {
    @Transaction
    @Query("select * from $CUSTOM_RAFFLE_TABLE_NAME where $CUSTOM_RAFFLE_ID_COLUMN_NAME = :id ")
    fun getCustomRaffleById(id: Long): CustomRaffleWithItemsDto

    @Transaction
    @Query("select * from $CUSTOM_RAFFLE_TABLE_NAME")
    fun getAllCustomRaffles(): List<CustomRaffleWithItemsDto>

    @Insert(onConflict = REPLACE)
    fun saveCustomRaffle(customRaffleDto: CustomRaffleDto): Long

    @Query("delete from $CUSTOM_RAFFLE_TABLE_NAME where $CUSTOM_RAFFLE_ID_COLUMN_NAME = :id")
    fun deleteCustomRaffleById(id: Long)
}

@Dao
interface CustomRaffleItemDao {
    @Insert(onConflict = REPLACE)
    fun saveCustomRaffleItems(vararg items: CustomRaffleItemDto)

    @Query("delete from $CUSTOM_RAFFLE_ITEM_TABLE_NAME where $CUSTOM_RAFFLE_ITEM_CUSTOM_RAFFLE_ID_COLUMN_NAME = :customRaffleId")
    fun deleteCustomRaffleItemsByCustomRaffleId(customRaffleId: Long)
}
