package ru.kovsh.tasku.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.kovsh.tasku.R
import ru.kovsh.tasku.models.areas.entities.Area
import ru.kovsh.tasku.ui.theme.Base0
import ru.kovsh.tasku.ui.theme.Base500
import ru.kovsh.tasku.ui.theme.DarkBlueBackground800
import ru.kovsh.tasku.ui.theme.DarkBlueBackground900
import ru.kovsh.tasku.ui.theme.PriorityHigh
import ru.kovsh.tasku.ui.theme.PriorityLow
import ru.kovsh.tasku.ui.theme.PriorityMedium
import ru.kovsh.tasku.ui.theme.typography

@Preview(showBackground = true, backgroundColor = 0xFF1E293E)
@Composable
fun PriorityPickDialog(
    onDismissDialog: () -> Unit = {},
    onPriorityPicked: (Int) -> Unit = {},
    curPriority: Int = 0,
) {
    val availablePriorities = listOf("None", "Low", "Medium", "High")
    Dialog(onDismissRequest = onDismissDialog) {
        Card(
            shape = RoundedCornerShape(10.dp),
            backgroundColor = DarkBlueBackground900
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_priority),
                        contentDescription = "Priority",
                        modifier = Modifier.size(24.dp),
                        tint = Base500
                    )
                    Text(
                        text = "Priority",
                        modifier = Modifier.padding(start = 4.dp),
                        style = typography.h4.copy(color = Base0)
                    )
                }
                LazyColumn(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(availablePriorities) { index, item ->
                        Box(
                            modifier = Modifier.clip(shape = RoundedCornerShape(5.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (curPriority == index)
                                            DarkBlueBackground800 else Color.Transparent
                                    ),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                TextButton(
                                    onClick = { onPriorityPicked(index) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    val iconColor = when (index) {
                                        1 -> PriorityLow
                                        2 -> PriorityMedium
                                        3 -> PriorityHigh
                                        else -> Base500
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_priority),
                                            contentDescription = "Increase priority",
                                            tint = iconColor
                                        )
                                        Text(
                                            text = item,
                                            modifier = Modifier.padding(start = 8.dp),
                                            style = typography.body1.copy(color = Base0)
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


@Preview(showBackground = true, backgroundColor = 0xFF1E293E)
@Composable
fun AreasPickDialog(
    onDismissDialog: () -> Unit = {},
    onAreaPicked: (Long) -> Unit = {},
    currentAreaID: Long = 1L,
    areas: List<Area> = listOf(Area(0L, "Area 0"), Area(1L, "Area 1"))
) {
    Dialog(onDismissRequest = onDismissDialog) {
        Card(
            shape = RoundedCornerShape(10.dp),
            backgroundColor = DarkBlueBackground900
        ) {
            Column(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_main_menu_area),
                        contentDescription = "Area",
                        modifier = Modifier.size(24.dp),
                    )
                    Text(
                        text = "Choose area",
                        modifier = Modifier.padding(start = 4.dp),
                        style = typography.h4.copy(color = Base0)
                    )
                }
                if (areas.isEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp).fillMaxWidth()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = Base500
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp).fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(areas) { index, area ->
                        Box(
                            modifier = Modifier.clip(shape = RoundedCornerShape(5.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (area.area_id == currentAreaID)
                                            DarkBlueBackground800 else Color.Transparent
                                    ),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                TextButton(
                                    onClick = { onAreaPicked(area.area_id) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_main_menu_area),
                                            contentDescription = "Increase priority",
                                        )
                                        Text(
                                            text = area.title,
                                            modifier = Modifier.padding(start = 8.dp),
                                            style = typography.body1.copy(color = Base0)
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


@Preview
@Composable
fun PrevEmptyAreasPickDialog() {
    AreasPickDialog(
        areas = listOf(),
    )
}