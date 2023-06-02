package capstone.project.trasholution

import capstone.project.trasholution.logic.repository.responses.DataItem

object DataDummy {

    fun generateDummyDataResponse(): List<DataItem> {
        val items: MutableList<DataItem> = arrayListOf()
        for (i in 0..100) {
            val story = DataItem(
                contact = i+80000,
                v = 0,
                location = "location_$i",
                id = i.toString(),
                username = "name_$i",
                createDate = "date_$i",
            )
            items.add(story)
        }
        return items
    }
}