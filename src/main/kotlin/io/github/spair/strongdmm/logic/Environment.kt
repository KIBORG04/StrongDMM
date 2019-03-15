package io.github.spair.strongdmm.logic

import io.github.spair.byond.ByondFiles
import io.github.spair.byond.dme.Dme
import io.github.spair.byond.dme.parser.DmeParser
import io.github.spair.dmm.io.reader.DmmReader
import io.github.spair.strongdmm.DI
import io.github.spair.strongdmm.gui.mapcanvas.MapCanvasController
import io.github.spair.strongdmm.gui.objtree.ObjectTreeController
import io.github.spair.strongdmm.logic.map.Dmm
import org.kodein.di.erased.instance
import java.io.File

class Environment {

    private lateinit var dme: Dme
    val availableMaps = mutableListOf<String>()

    private val objectTreeController by DI.instance<ObjectTreeController>()
    private val mapCanvasController by DI.instance<MapCanvasController>()

    fun parseAndPrepareEnv(dmeFile: File): Dme {
        val s = System.currentTimeMillis()

        dme = DmeParser.parse(dmeFile)

        objectTreeController.populateTree(dme)
        findAvailableMaps(dmeFile.parentFile)
        System.gc()

        println(System.currentTimeMillis() - s)

        return dme
    }

    fun openMap(mapPath: String) {
        val dmmData = DmmReader.readMap(File(mapPath))
        val dmm = Dmm(dmmData, dme)
        mapCanvasController.selectMap(dmm)
    }

    fun getAbsolutePath() = dme.absoluteRootPath!!

    private fun findAvailableMaps(rootFolder: File) {
        rootFolder.walkTopDown().forEach {
            if (it.path.endsWith(ByondFiles.DMM_SUFFIX)) {
                availableMaps.add(it.path)
            }
        }
    }
}
