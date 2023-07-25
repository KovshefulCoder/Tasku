package ru.kovsh.tasku.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kovsh.tasku.R
import ru.kovsh.tasku.models.tasks.enities.Task
import ru.kovsh.tasku.ui.theme.AccentBlue
import ru.kovsh.tasku.ui.theme.Base0
import ru.kovsh.tasku.ui.theme.Base500
import ru.kovsh.tasku.ui.theme.DarkBlueBackground900
import ru.kovsh.tasku.ui.theme.PlaceholderText
import ru.kovsh.tasku.ui.theme.PriorityHigh
import ru.kovsh.tasku.ui.theme.PriorityLow
import ru.kovsh.tasku.ui.theme.PriorityMedium
import ru.kovsh.tasku.ui.theme.TaskBackground
import ru.kovsh.tasku.ui.theme.typography
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


@Preview
@Composable
fun FullEmptyTaskExpandedButton() {
    TaskExpandedButton(
        task = Task(
            area_id = 0L,
            checked = false,
            date = "",
            deadline = "",
            description = "",
            name = "",
            priority = -1,
            tag = "",
            task_id = 0L,
            title = "",
            inbox = false,
            someday = false,
        )
    )

}

@Preview
@Composable
fun HalfEmptyTaskExpandedButton() {
    TaskExpandedButton(
        task = Task(
            area_id = 1L,
            checked = false,
            date = "",
            deadline = "09.10",
            description = "",
            name = "Area",
            priority = 1,
            tag = "",
            task_id = 0L,
            title = "",
            inbox = false,
            someday = false,
        ),
    )

}

@OptIn(ExperimentalLayoutApi::class, ExperimentalComposeUiApi::class)
@Preview
@Composable
fun TaskExpandedButton(
    onAdditionalInfoButtonClicked: (AdditionalInfoEntities, Task) -> Unit = { _, _ -> },
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
        title = "",
        inbox = false,
        someday = false,
    ),
    onCreateTask: (Task) -> Unit = {},
    listIndex: Int = -1,
    isNewTask: Boolean = false,
    focusRequester: FocusRequester = FocusRequester(),
) {
    val (textColor, iconTaskID) = applyTaskStatus(task.checked ?: false)
    val (isAdditionalInfo, additionalInfo) = processAdditionalInfo(task)

    val titleInput = remember { mutableStateOf(task.title) }
    //val titleInput = remember { mutableStateOf("") }
    val titlePlaceholder = when (titleInput.value) {
        "" -> stringResource(id = R.string.placeholder_task_title) + stringResource(id = R.string.ellipsis)
        else -> ""
    }
    val descriptionInput = remember { mutableStateOf(task.description) }
    //val descriptionInput = remember { mutableStateOf("") }
    val descriptionPlaceholder = when (descriptionInput.value) {
        "", null -> stringResource(id = R.string.placeholder_task_description) + stringResource(id = R.string.ellipsis)
        else -> ""
    }

    val keyboardController = LocalSoftwareKeyboardController.current


    Box(modifier = Modifier.clip(RoundedCornerShape(10)))
    {
        Column(
            modifier = Modifier
                .background(TaskBackground)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        verticalArrangement = Arrangement.Top
                    ) {
                        Image(
                            painter = painterResource(id = iconTaskID),
                            contentDescription = "Task Icon",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        BasicTextField(
                            value = titleInput.value,
                            onValueChange = {
                                titleInput.value = it
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            textStyle = typography.h5.copy(
                                color = textColor,
                                fontWeight = FontWeight.Normal
                            ),
                            cursorBrush = SolidColor(AccentBlue),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    if (isNewTask) {
                                        onCreateTask(
                                            task.copy(
                                                title = titleInput.value,
                                                priority = if (task.priority == -1) 0 else task.priority
                                            )
                                        )
                                    } else {
                                        onAdditionalInfoButtonClicked(
                                            AdditionalInfoEntities.Title,
                                            task.copy(title = titleInput.value)
                                        )
                                    }
                                }
                            ),
                            decorationBox = { innerTextField ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth()
                                    ) {
                                        if (titlePlaceholder.isNotEmpty()) {
                                            Text(
                                                text = titlePlaceholder,
                                                style = typography.h5.copy(color = PlaceholderText)
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        BasicTextField(
                            value = descriptionInput.value ?: "",
                            onValueChange = {
                                descriptionInput.value = it
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            textStyle = typography.caption,
                            cursorBrush = SolidColor(AccentBlue),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    onAdditionalInfoButtonClicked(
                                        AdditionalInfoEntities.Description,
                                        task.copy(description = descriptionInput.value)
                                    )
                                }
                            ),
                            decorationBox = { innerTextField ->
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth()
                                    ) {
                                        if (descriptionPlaceholder.isNotEmpty()) {
                                            Text(
                                                text = descriptionPlaceholder,
                                                style = typography.caption.copy(
                                                    color = PlaceholderText,
                                                    fontStyle = FontStyle.Italic
                                                )
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                if (isAdditionalInfo) {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            FlowRow(
                                modifier = Modifier.weight(2.5f),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                if (additionalInfo.area_id != -1L && additionalInfo.name != "") {
                                    ButtonAdditionalInfo(
                                        obButtonClicked = {
                                            onAdditionalInfoButtonClicked(
                                                AdditionalInfoEntities.Area,
                                                task
                                            )
                                        },
                                    ) {
                                        AreaAdditionalInfo(
                                            additionalInfo.name,
                                            buttonStatus = AdditionalInfoStatus.ActiveExpanded,
                                        )
                                    }
                                }
                                if (additionalInfo.date != "") {
                                    Spacer(modifier = Modifier.padding(start = 4.dp))
                                    ButtonAdditionalInfo(
                                        obButtonClicked = {
                                            onAdditionalInfoButtonClicked(
                                                AdditionalInfoEntities.Date,
                                                task
                                            )
                                        },
                                    ) {
                                        DateAdditionalInfo(
                                            additionalInfo.date,
                                            buttonStatus = AdditionalInfoStatus.ActiveExpanded
                                        )
                                    }
                                }
                                if (additionalInfo.tag != "") {
                                    Spacer(modifier = Modifier.padding(start = 4.dp))
                                    ButtonAdditionalInfo(
                                        obButtonClicked = {
                                            onAdditionalInfoButtonClicked(
                                                AdditionalInfoEntities.Tag,
                                                task
                                            )
                                        },
                                    ) {
                                        TagAdditionalInfo(
                                            additionalInfo.tag,
                                            buttonStatus = AdditionalInfoStatus.ActiveExpanded
                                        )
                                    }
                                }
                                if (additionalInfo.priority >= 0) {
                                    Spacer(modifier = Modifier.padding(start = 4.dp))
                                    ButtonAdditionalInfo(
                                        obButtonClicked = {
                                            onAdditionalInfoButtonClicked(
                                                AdditionalInfoEntities.Priority,
                                                task
                                            )
                                        },
                                    ) {
                                        PriorityAdditionalInfo(
                                            taskPriority = additionalInfo.priority,
                                            buttonStatus = AdditionalInfoStatus.ActiveExpanded
                                        )
                                    }
                                }
                                //Inactive
                                if (additionalInfo.area_id == -1L || additionalInfo.name == "") {
                                    ButtonAdditionalInfo(
                                        obButtonClicked = {
                                            onAdditionalInfoButtonClicked(
                                                AdditionalInfoEntities.Area,
                                                task
                                            )
                                        },
                                        isActive = false
                                    ) {
                                        AreaAdditionalInfo(
                                            "Area",
                                            buttonStatus = AdditionalInfoStatus.InactiveExpanded
                                        )
                                    }
                                }
                                if (additionalInfo.date == "") {
                                    Spacer(modifier = Modifier.padding(start = 4.dp))
                                    ButtonAdditionalInfo(
                                        obButtonClicked = {
                                            onAdditionalInfoButtonClicked(
                                                AdditionalInfoEntities.Date,
                                                task
                                            )
                                        },
                                        isActive = false,
                                    ) {
                                        DateAdditionalInfo(
                                            "Date",
                                            buttonStatus = AdditionalInfoStatus.InactiveExpanded
                                        )
                                    }
                                }
                                if (additionalInfo.tag == "") {
                                    Spacer(modifier = Modifier.padding(start = 4.dp))
                                    ButtonAdditionalInfo(
                                        obButtonClicked = {
                                            onAdditionalInfoButtonClicked(
                                                AdditionalInfoEntities.Tag,
                                                task
                                            )
                                        },
                                        isActive = false
                                    ) {
                                        TagAdditionalInfo(
                                            "Tag",
                                            buttonStatus = AdditionalInfoStatus.InactiveExpanded
                                        )
                                    }
                                }
                                if (additionalInfo.priority == -1) {
                                    Spacer(modifier = Modifier.padding(start = 4.dp))
                                    ButtonAdditionalInfo(
                                        obButtonClicked = {
                                            onAdditionalInfoButtonClicked(
                                                AdditionalInfoEntities.Priority,
                                                task
                                            )
                                        },
                                        isActive = false
                                    ) {
                                        PriorityAdditionalInfo(
                                            isNewTask = isNewTask,
                                            taskPriority = -1,
                                            buttonStatus = AdditionalInfoStatus.InactiveExpanded
                                        )
                                    }
                                }
                            }
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                if (additionalInfo.deadline != "") {
                                    ButtonAdditionalInfo(
                                        obButtonClicked = {
                                            onAdditionalInfoButtonClicked(
                                                AdditionalInfoEntities.Deadline,
                                                task
                                            )
                                        },
                                    ) {
                                        DeadlineAdditionalInfo(
                                            taskDeadline = additionalInfo.deadline,
                                            buttonStatus = AdditionalInfoStatus.ActiveExpanded
                                        )
                                    }
                                } else {
                                    ButtonAdditionalInfo(
                                        obButtonClicked = {
                                            onAdditionalInfoButtonClicked(
                                                AdditionalInfoEntities.Deadline,
                                                task
                                            )
                                        },
                                        isActive = false,
                                    ) {
                                        DeadlineAdditionalInfo(
                                            taskDeadline = "Deadline",
                                            buttonStatus = AdditionalInfoStatus.InactiveExpanded
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class AdditionalInfoStatus {
    ActiveExpanded,
    ActiveCollapsed,
    InactiveExpanded
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ButtonAdditionalInfo(
    obButtonClicked: () -> Unit = {},
    isActive: Boolean = true,
    additionalInfo: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        val backgroundColor = if (isActive) {
            Color.Transparent
        } else {
            DarkBlueBackground900
        }
        Button(
            onClick = obButtonClicked,
            modifier = Modifier.padding(top = 2.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = backgroundColor,
            ),
            elevation = null
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                additionalInfo()
            }
        }
    }
}


@Composable
fun AreaAdditionalInfo(takeAreaName: String, buttonStatus: AdditionalInfoStatus) {
    val (alphaValue, textColor) = when (buttonStatus) {
        AdditionalInfoStatus.ActiveExpanded -> Pair(1f, Base0)
        else -> Pair(0.5f, Base500)
    }
    val (textStyle, iconSize) = when (buttonStatus) {
        AdditionalInfoStatus.ActiveCollapsed -> Pair(typography.overline, 12.dp)
        else -> Pair(typography.caption, 18.dp)
    }
    Image(
        painter = painterResource(id = R.drawable.ic_main_menu_area),
        modifier = Modifier
            .size(iconSize)
            .alpha(alphaValue),
        contentDescription = "Area Icon",
    )
    Text(
        text = takeAreaName,
        modifier = Modifier.padding(start = 2.dp),
        style = textStyle.copy(color = textColor)
    )
}

@Composable
fun DateAdditionalInfo(taskDate: String, buttonStatus: AdditionalInfoStatus) {
    val (alphaValue, textColor) = when (buttonStatus) {
        AdditionalInfoStatus.ActiveExpanded -> Pair(1f, Base0)
        else -> Pair(0.5f, Base500)
    }
    val (textStyle, iconSize) = when (buttonStatus) {
        AdditionalInfoStatus.ActiveCollapsed -> Pair(typography.overline, 12.dp)
        else -> Pair(typography.caption, 18.dp)
    }
    Image(
        painter = painterResource(id = R.drawable.ic_main_menu_upcoming),
        modifier = Modifier
            .size(iconSize)
            .alpha(alphaValue),
        contentDescription = "Date Icon",
    )
    Text(
        text = if (taskDate.isEmpty()) "" else convertISOLongTimePatternToDayMonth(taskDate),
        modifier = Modifier.padding(start = 2.dp),
        style = textStyle.copy(color = textColor)
    )
}

@Composable
fun TagAdditionalInfo(taskTag: String, buttonStatus: AdditionalInfoStatus) {
    val (alphaValue, textColor) = when (buttonStatus) {
        AdditionalInfoStatus.ActiveExpanded -> Pair(1f, Base0)
        else -> Pair(0.5f, Base500)
    }
    val (textStyle, iconSize) = when (buttonStatus) {
        AdditionalInfoStatus.ActiveCollapsed -> Pair(typography.overline, 12.dp)
        else -> Pair(typography.caption, 18.dp)
    }
    Image(
        painter = painterResource(id = R.drawable.ic_tag),
        modifier = Modifier
            .size(iconSize)
            .alpha(alphaValue),
        contentDescription = "Tag Icon",
    )
    Text(
        text = taskTag,
        modifier = Modifier.padding(start = 2.dp),
        style = textStyle.copy(color = textColor)
    )
}

@Composable
fun PriorityAdditionalInfo(
    isNewTask: Boolean = false,
    taskPriority: Int,
    buttonStatus: AdditionalInfoStatus
) {
    val (alphaValue, textColor) = when (buttonStatus) {
        AdditionalInfoStatus.ActiveExpanded -> Pair(1f, Base0)
        else -> Pair(0.5f, Base500)
    }
    val (textStyle, iconSize) = when (buttonStatus) {
        AdditionalInfoStatus.ActiveCollapsed -> Pair(typography.overline, 12.dp)
        else -> Pair(typography.caption, 18.dp)
    }
    val priorityText = when (taskPriority) {
        0 -> "None"
        1 -> "Low"
        2 -> "Medium"
        3 -> "High"
        else -> ""
    }
    Icon(
        painter = painterResource(id = R.drawable.ic_priority),
        modifier = Modifier
            .size(iconSize)
            .alpha(alphaValue),
        contentDescription = "Priority Icon",
        tint = when (taskPriority) {
            1 -> PriorityLow
            2 -> PriorityMedium
            3 -> PriorityHigh
            else -> Base500
        }
    )
    Text(
        text = if (isNewTask) "Priority" else priorityText,
        modifier = Modifier.padding(start = 2.dp),
        style = textStyle.copy(color = textColor)
    )
}

@Composable
fun DeadlineAdditionalInfo(taskDeadline: String, buttonStatus: AdditionalInfoStatus) {
    val (alphaValue, textColor) = when (buttonStatus) {
        AdditionalInfoStatus.ActiveExpanded -> Pair(1f, Base0)
        else -> Pair(0.5f, Base500)
    }
    val (textStyle, iconSize) = when (buttonStatus) {
        AdditionalInfoStatus.ActiveCollapsed -> Pair(typography.overline, 12.dp)
        else -> Pair(typography.caption, 18.dp)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_deadline),
            modifier = Modifier
                .size(iconSize)
                .alpha(alphaValue),
            contentDescription = "Deadline Icon",
        )
        Text(
            text = if (taskDeadline.isEmpty()) "" else convertISOLongTimePatternToDayMonth(taskDeadline),
            modifier = Modifier.padding(start = 2.dp),
            style = textStyle.copy(color = textColor)
        )
    }
}

fun convertISOLongTimePatternToDayMonth(date: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    var dateTime: LocalDateTime
    try {
        dateTime = LocalDateTime.parse(date, inputFormatter)
    } catch (e: DateTimeParseException) {
        return date
    }
    val outputFormatter = DateTimeFormatter.ofPattern("dd.MM")
    return dateTime.format(outputFormatter)
}