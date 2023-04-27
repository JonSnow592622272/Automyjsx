package org.automyjsa.automyjsa.model.project

import com.stardust.automyjsa.project.ProjectConfig
import com.stardust.automyjsa.project.ProjectConfig.Companion.configFileOfDir
import com.stardust.pio.PFiles.write
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ProjectTemplate(private val mProjectConfig: ProjectConfig, private val mProjectDir: File) {

    suspend fun newProject(): File = withContext(Dispatchers.IO) {
        mProjectDir.mkdirs()
        write(configFileOfDir(mProjectDir.path), mProjectConfig.toJson())
        File(mProjectDir, mProjectConfig.mainScript!!).createNewFile()
        mProjectDir
    }
}