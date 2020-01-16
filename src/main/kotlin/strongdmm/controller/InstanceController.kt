package strongdmm.controller

import strongdmm.byond.*
import strongdmm.byond.dmi.GlobalDmiHolder
import strongdmm.byond.dmm.GlobalTileItemHolder
import strongdmm.byond.dmm.TileItem
import strongdmm.event.Event
import strongdmm.event.EventConsumer
import strongdmm.event.EventSender

class InstanceController : EventConsumer, EventSender {
    init {
        consumeEvent(Event.InstanceController.GenerateFromIconStates::class.java, ::handleGenerateFromIconStates)
        consumeEvent(Event.InstanceController.GenerateFromDirections::class.java, ::handleGenerateFromDirections)
    }

    private fun handleGenerateFromIconStates(event: Event<TileItem, Unit>) {
        GlobalDmiHolder.getDmi(event.body.icon)?.let { dmi ->
            sendEvent(Event.EnvironmentController.Fetch { dme ->
                val itemType = event.body.type
                val dmeItem = dme.getItem(itemType)!!
                val initialIconState = dmeItem.getVarText(VAR_ICON_STATE)

                dmi.iconStates.keys.filter { it != initialIconState }.let { iconStates ->
                    if (iconStates.isNotEmpty()) {
                        iconStates.forEach { iconStateName ->
                            GlobalTileItemHolder.getOrCreate(itemType, mutableMapOf(VAR_ICON_STATE to "\"$iconStateName\""))
                        }
                        event.reply(Unit)
                    }
                }
            })
        }
    }

    private fun handleGenerateFromDirections(event: Event<TileItem, Unit>) {
        val tileItem = event.body
        GlobalDmiHolder.getIconState(tileItem.icon, tileItem.iconState)?.let { iconState ->
            sendEvent(Event.EnvironmentController.Fetch { dme ->
                val dmeItem = dme.getItem(tileItem.type)!!
                val initialDir = dmeItem.getVarInt(VAR_DIR)

                when (iconState.dirs) {
                    4 -> {
                        arrayOf(NORTH, SOUTH, EAST, WEST).filter { it != initialDir }.forEach { dir ->
                            GlobalTileItemHolder.getOrCreate(tileItem.type, mutableMapOf(VAR_DIR to dir.toString()))
                        }
                        event.reply(Unit)
                    }
                    8 -> {
                        arrayOf(NORTH, SOUTH, EAST, WEST, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST).filter { it != initialDir }.forEach { dir ->
                            GlobalTileItemHolder.getOrCreate(tileItem.type, mutableMapOf(VAR_DIR to dir.toString()))
                        }
                        event.reply(Unit)
                    }
                }
            })
        }
    }
}
