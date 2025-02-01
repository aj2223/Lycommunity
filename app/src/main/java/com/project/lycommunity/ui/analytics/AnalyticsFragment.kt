package com.project.lycommunity.ui.analytics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import android.graphics.Color
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.google.android.material.snackbar.Snackbar
import com.project.lycommunity.R
import com.project.lycommunity.databinding.FragmentAnalyticsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AnalyticsFragment : Fragment() {

    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!

    private val analyticsViewModel: AnalyticsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        observeUIStates()
    }

    private fun observeUIStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                analyticsViewModel.uiState.collect { state ->
                    when (state) {
                        is AnalyticsUIState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.pieChart.visibility = View.GONE
                            binding.barChartLikes.visibility = View.GONE
                        }
                        is AnalyticsUIState.Success -> {
                            binding.progressBar.visibility = View.GONE

                            // Display User Analytics
                            binding.tvTotalUsers.text = "Total Users: ${state.totalUsers}"
                            binding.tvActiveUsers.text = "Active Users: ${state.activeUsers}"
                            binding.tvDormantUsers.text = "Dormant Users: ${state.dormantUsers}"
                            setupPieChart(state.totalUsers, state.activeUsers, state.dormantUsers)

                            // Display Most Liked Data
                            showMostLikedData(
                                state.mostLikedAnnouncements,
                                state.mostLikedEvents
                            )
                        }
                        is AnalyticsUIState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }



    private fun showAnalyticsData(
        totalUsers: Int,
        activeUsers: Int,
        dormantUsers: Int
    ) {
        // Prepare data for the BarChart
        val entries = listOf(
            BarEntry(0f, totalUsers.toFloat()),  // Total Users
            BarEntry(1f, activeUsers.toFloat()), // Active Users
            BarEntry(2f, dormantUsers.toFloat()) // Dormant Users
        )

        // Create a dataset and label it
        val dataSet = BarDataSet(entries, "User Analytics").apply {
            setColors(
                resources.getColor(R.color.colorFb, null),
                resources.getColor(R.color.ColorBadge, null),
                resources.getColor(R.color.ColorFilterPurple, null)
            )
            valueTextSize = 12f
        }

        // Create BarData from the dataset
        val barData = BarData(dataSet)

        // Bind the data to the BarChart and refresh
        binding.barChartLikes.data = barData
        binding.barChartLikes.invalidate() // Refresh the chart
    }


        private fun setupPieChart(totalUsers: Int, activeUsers: Int, dormantUsers: Int) {
        val entries = listOf(
            PieEntry(activeUsers.toFloat(), "Active"),
            PieEntry(dormantUsers.toFloat(), "Dormant")
        )

        val dataSet = PieDataSet(entries, "User Analytics").apply {
            colors = listOf(Color.GREEN, Color.RED)
            valueTextSize = 14f
            valueTextColor = Color.WHITE
        }

        binding.pieChart.apply {
            data = PieData(dataSet)
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.TRANSPARENT)
            centerText = "Total Users\n$totalUsers"
            setCenterTextSize(16f)
            animateY(1000)
            invalidate()
            visibility = View.VISIBLE
        }
    }

    private fun showMostLikedData(
        announcements: List<Pair<String, Int>>,
        events: List<Pair<String, Int>>
    ) {
        val entries = mutableListOf<BarEntry>()
        val labels = mutableListOf<String>()

        announcements.forEachIndexed { index, (title, likes) ->
            entries.add(BarEntry(index.toFloat(), likes.toFloat()))
            labels.add("A: $title")
        }

        events.forEachIndexed { index, (title, likes) ->
            entries.add(BarEntry((announcements.size + index).toFloat(), likes.toFloat()))
            labels.add("E: $title")
        }

        val dataSet = BarDataSet(entries, "Most Liked").apply {
            setColors(Color.BLUE, Color.CYAN)
            valueTextSize = 12f
        }

        binding.barChartLikes.apply {
            data = BarData(dataSet)
            description.isEnabled = false
            invalidate()
            visibility = View.VISIBLE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
