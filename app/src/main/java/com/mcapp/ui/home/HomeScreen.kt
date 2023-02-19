package com.mcapp.ui.home

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.mcapp.data.entity.Reminder
import com.mcapp.ui.reminder.ReminderViewState
import com.mcapp.ui.reminder.ReminderViewModel
import com.mcapp.util.AppStatus
import org.koin.androidx.compose.get
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(reminderViewModel: ReminderViewModel, context: Context, isAuth: MutableState<Boolean>) {
    val appStatus: AppStatus = get()
    var isMakingNewReminder by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isMakingNewReminder = true },
                contentColor = Color.Blue,
                modifier = Modifier.padding(all = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(all = 5.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        if (isMakingNewReminder) {
            MakeNewReminder(
                viewModel = reminderViewModel,
                onBack = { isMakingNewReminder = false }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .systemBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ReminderList(reminderViewModel, it)
                Spacer(modifier = Modifier
                    .padding(bottom = 20.dp)
                    .height(10.dp))
                Spacer(modifier = Modifier
                    .padding(top = 20.dp)
                    .height(10.dp))
                Button(
                    onClick = {
                        appStatus.logout(context, isAuth)
                    },
                    enabled = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(45.dp),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(text = "Log out")
                }
            }
        }
    }
}

@Composable
private fun ReminderList(
    reminderViewModel: ReminderViewModel,
    it: PaddingValues
) {
    reminderViewModel.getListOfAllReminders(0)

    val reminderViewState by reminderViewModel.reminders.collectAsState()
    when (reminderViewState) {
        is ReminderViewState.Loading -> {println("ReminderViewState = Loading")}
        is ReminderViewState.Success -> {
            println("ReminderViewState = OK")
            val reminders = (reminderViewState as ReminderViewState.Success).data
            println("Found reminders: $reminders")  // debug

            LazyColumn(
                contentPadding = PaddingValues(0.dp),
                verticalArrangement = Arrangement.Center
            ) {
                items(reminders) { item ->
                    println("Found reminder: " + item.message)  // debug
                    ReminderListItem(
                        reminder = item,
                        onClick = {},
                        it = it
                    )
                }
            }
        }
    }
}

@Composable
private fun ReminderListItem(
    reminder: Reminder,
    onClick: () -> Unit,
    it: PaddingValues,
    modifier: Modifier = Modifier,
) {
    ConstraintLayout(
        modifier = modifier.clickable { onClick() }.padding(it)
    ) {
        val (dividerRef, titleRef, iconRef) = createRefs()
        Divider(
            Modifier.constrainAs(dividerRef) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )

        // title
        Text(
            text = reminder.message,
            maxLines = 1,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.constrainAs(titleRef) {
                linkTo(
                    start = parent.start,
                    end = iconRef.start,
                    startMargin = 24.dp,
                    endMargin = 16.dp,
                    bias = 0f // float this towards the start. this was is the fix we needed
                )
                top.linkTo(parent.top, margin = 10.dp)
                width = Dimension.preferredWrapContent
            }
        )

//        // category
//        Text(
//            text = category.name,
//            maxLines = 1,
//            style = MaterialTheme.typography.subtitle2,
//            modifier = Modifier.constrainAs(categoryRef) {
//                linkTo(
//                    start = parent.start,
//                    end = iconRef.start,
//                    startMargin = 24.dp,
//                    endMargin = 8.dp,
//                    bias = 0f // float this towards the start. this was is the fix we needed
//                )
//                top.linkTo(titleRef.bottom, margin = 6.dp)
//                bottom.linkTo(parent.bottom, 10.dp)
//                width = Dimension.preferredWrapContent
//            }
//        )

//        // date
//        Text(
//            text = reminder.date.toString(),
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis,
//            style = MaterialTheme.typography.caption,
//            modifier = Modifier.constrainAs(dateRef) {
//                linkTo(
//                    start = categoryRef.end,
//                    end = iconRef.start,
//                    startMargin = 8.dp,
//                    endMargin = 16.dp,
//                    bias = 0f // float this towards the start. this was is the fix we needed
//                )
//                centerVerticallyTo(categoryRef)
//                top.linkTo(titleRef.bottom, 6.dp)
//                bottom.linkTo(parent.bottom, 10.dp)
//            }
//        )

        // icon
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .size(50.dp)
                .padding(6.dp)
                .constrainAs(iconRef) {
                    top.linkTo(parent.top, 10.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                    end.linkTo(parent.end)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = ""
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MakeNewReminder(viewModel: ReminderViewModel, onBack: () -> Unit) {
    val message = remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            value = message.value,
            onValueChange = { message.value = it },
            label = { Text(text = "Reminder message:") },
            modifier = Modifier
                .padding(all = 2.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier
            .padding(2.dp)
            .height(10.dp))

        Spacer(modifier = Modifier
            .padding(2.dp)
            .height(10.dp))
        Button(
            onClick = {
                    viewModel.insertOrUpdateReminder(
                        Reminder(
                            message = message.value,
                            locationX = 0,
                            locationY = 0,
                            reminderTime = LocalDateTime.of(
                                2023, 2, 15, 19, 29),
                            creationTime = LocalDateTime.now(),
                            creatorId = 0,
                            reminderSeen = false
                        )
                    )
                    onBack()
            },
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth()
                .size(55.dp)
        ) {
            Text("Save reminder")
        }
    }
}
