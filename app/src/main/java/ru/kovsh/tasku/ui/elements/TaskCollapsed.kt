package ru.kovsh.tasku.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kovsh.tasku.R
import ru.kovsh.tasku.models.tasks.enities.Task
import ru.kovsh.tasku.ui.theme.Base0
import ru.kovsh.tasku.ui.theme.Base500
import ru.kovsh.tasku.ui.theme.TaskBackground
import ru.kovsh.tasku.ui.theme.typography

@Preview
@Composable
fun TaskCollapsedButton(
    task: Task = Task(
        area_id = 0L,
        checked = false,
        date = "",
        deadline = "",
        description = "",
        name = "",
        priority = -1,
        tag = "",
        task_id = 0L,
        title = "Task",
        inbox = false,
        someday = false,
    ),
    onTaskClicked: () -> Unit = {},
) {
    val buttonVerticalPadding = remember { mutableStateOf(8.dp) }
    val (textColor, iconTaskID) = applyTaskStatus(task.checked?:false)
    val (isAdditionalInfo, additionalInfo) = processAdditionalInfo(task)
    if (isAdditionalInfo) {
        buttonVerticalPadding.value = 6.dp
    }
    Button(
        onClick = onTaskClicked ,
        shape = RoundedCornerShape(100),
        colors = ButtonDefaults.buttonColors(backgroundColor = TaskBackground),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = buttonVerticalPadding.value)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = iconTaskID),
                contentDescription = "Task Icon",
            )
            TaskContent(
                title = task.title,
                textColor = textColor,
                isAdditionalInfo = isAdditionalInfo,
                additionalInfo = additionalInfo,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}


@Composable
fun TaskContent(
    title: String,
    textColor: Color,
    isAdditionalInfo: Boolean,
    additionalInfo: TaskAdditionalInfo,
    modifier: Modifier = Modifier
) {
    if (!isAdditionalInfo) {
        Text(
            modifier = modifier,
            text = title,
            color = textColor,
            style = typography.body1
        )
    } else {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                color = textColor,
                style = typography.body1
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    if (additionalInfo.area_id != -1L && additionalInfo.name != "") {
                        AreaAdditionalInfo(takeAreaName = additionalInfo.name, buttonStatus = AdditionalInfoStatus.ActiveCollapsed)
                    }
                    if (additionalInfo.date != "") {
                        Spacer(modifier = Modifier.padding(start = 4.dp))
                        DateAdditionalInfo(taskDate = additionalInfo.date, buttonStatus = AdditionalInfoStatus.ActiveCollapsed)
                    }
                    if (additionalInfo.tag != "") {
                        Spacer(modifier = Modifier.padding(start = 4.dp))
                        TagAdditionalInfo(taskTag = additionalInfo.tag, buttonStatus = AdditionalInfoStatus.ActiveCollapsed)
                    }
                    if (additionalInfo.priority > 0) {
                        Spacer(modifier = Modifier.padding(start = 4.dp))
                        PriorityAdditionalInfo(taskPriority = additionalInfo.priority, buttonStatus = AdditionalInfoStatus.ActiveCollapsed)
                    }
                }
                if (additionalInfo.deadline != "") {
                    DeadlineAdditionalInfo(
                        taskDeadline = additionalInfo.deadline, buttonStatus = AdditionalInfoStatus.ActiveCollapsed)
                }
            }
        }
    }
}

data class TaskAdditionalInfo(
    val task_id: Long = -1L,
    val area_id: Long = -1L,
    val name: String = "",
    val date: String = "",
    val tag: String = "",
    val priority: Int = -1,
    val deadline: String = "",
    val title: String = "",
    val description: String? = "",
)

private fun checkTaskHaveAdditionalInfo(info: TaskAdditionalInfo): Boolean {
    //Empty additional info
    return info != TaskAdditionalInfo(
        area_id = -1L,
        name = "",
        deadline = "",
        priority = -1,
        tag = ""
    )
}

enum class TaskStatuses(val status: Boolean) {
//    Active("Active"),
//    Finished("Finished"),
//    Canceled("Canceled"),
//    Repeated("Repeated");

//    TaskStatuses.Finished -> {
//        textColor = Base500
//        R.drawable.ic_finished_task
//    }
//
//    TaskStatuses.Canceled -> {
//        textColor = Base500
//        R.drawable.ic_canceled_task
//    }
//
//    TaskStatuses.Repeated -> R.drawable.ic_repeated_task
//    TaskStatuses.Active -> R.drawable.ic_active_task

    Active(false),
    Finished(true);

    //    companion object {
//        fun convertToString(statusString: String): TaskStatuses? {
//            return values().find { it.status.equals(statusString, ignoreCase = true) }
//        }
//    }
    companion object {
        fun convertFromBool(statusBool: Boolean): TaskStatuses? {
            return values().find { it.status == statusBool }
        }
    }
}

fun applyTaskStatus(taskChecked: Boolean): Pair<Color, Int> {
    var textColor: Color = Base0
    val iconTaskID: Int = when (TaskStatuses.convertFromBool(taskChecked)) {
        TaskStatuses.Active -> {
            R.drawable.ic_active_task
        }
        else -> {
            textColor = Base500
            R.drawable.ic_finished_task
        }
    }
    return Pair(textColor, iconTaskID)
}

fun processAdditionalInfo(task: Task): Pair<Boolean, TaskAdditionalInfo> {
    val isAdditionalInfo = checkTaskHaveAdditionalInfo(
        TaskAdditionalInfo(
            area_id = task.area_id?:0L,
            name = task.name?:"",
            deadline = task.deadline?:"",
            priority = task.priority?:-1,
            tag = task.tag?:""
        )
    )
    var additionalInfo = TaskAdditionalInfo()
    if (isAdditionalInfo) {
        additionalInfo = TaskAdditionalInfo(
            area_id = task.area_id?:0L,
            name = task.name?:"",
            date = task.date?:"",
            tag = task.tag?:"",
            priority = task.priority?:-1,
            deadline = task.deadline?:"",
        )
    }
    return Pair(isAdditionalInfo, additionalInfo)
}
