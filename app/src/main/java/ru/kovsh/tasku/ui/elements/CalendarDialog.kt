package ru.kovsh.tasku.ui.elements

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import ru.kovsh.tasku.ui.theme.AccentBlue
import ru.kovsh.tasku.ui.theme.DarkBlueBackground800
import ru.kovsh.tasku.ui.theme.DarkBlueBackground900
import ru.kovsh.tasku.ui.theme.typography
import java.time.LocalDate
import java.time.YearMonth


@Preview(showBackground = true, backgroundColor = 0xFF1E293E)
@Composable
fun CalendarDialog(
    onDismissDialog: () -> Unit = {},
    onDayPicked: (LocalDate) -> Unit = { _ -> },
    initialDay: LocalDate = LocalDate.now()
    //currentDay: LocalDate = LocalDate(2023, 5, 31)
) {
    val calendarState = rememberSelectableCalendarState(
        initialMonth = YearMonth.now(),
        initialSelection = listOf(initialDay),
        initialSelectionMode = SelectionMode.Single,
        confirmSelectionChange = {
            Log.i("CalendarDialog", "onDayPicked: ${it[0]}")
            true
        }
    )
    Dialog(onDismissRequest = onDismissDialog) {
        Card(
            shape = RoundedCornerShape(10.dp),
            backgroundColor = DarkBlueBackground800
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SelectableCalendar(
                    calendarState = calendarState,
                    showAdjacentMonths = false,
                )
                Button(
                    onClick = {
                        onDayPicked(calendarState.selectionState.selection[0])
                    },
                    modifier = Modifier.padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = DarkBlueBackground900
                    )
                ) {
                    Text(
                        text = "Submit",
                        style = typography.button,
                        color = AccentBlue
                    )
                }
            }
        }
    }
}
