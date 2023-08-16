package ru.mvrlrd.playlistmaker.player.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.databinding.FragmentPlayerBinding
import ru.mvrlrd.playlistmaker.player.data.MyMediaPlayer
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.search.data.model.mapTrackToTrackForPlayer
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter
import ru.mvrlrd.playlistmaker.search.util.Debouncer
import ru.mvrlrd.playlistmaker.tools.loadImage


class PlayerFragment : Fragment() {
    private var page = 0

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private val playlistAdapter: PlaylistAdapterForPlayer by inject()
    private val args by navArgs<PlayerFragmentArgs>()
    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(parseIntent(args.track))
    }

    private lateinit var mService: PlayerService
    private var mBound: Boolean = false

    /** Defines callbacks for service binding, passed to bindService().  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance.
            val binder = service as PlayerService.LocalBinder
            mService = binder.getService()
            obserePreperingForService()
            observeCurrentPlayingTrackId()
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }


    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onStart() {
        super.onStart()
        // Bind to LocalService.

        Intent(requireActivity(), PlayerService::class.java).also { intent ->
            requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun observeCurrentPlayingTrackId(){
        viewLifecycleOwner.lifecycleScope.launch {
            mService.curr.collect(){
                Log.e(TAG, "observeCurrentPlayingTrackId: _____ $it", )
                viewModel.currentPlayingTrackId = it
                viewModel.playingTrack()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBottomSheet()
        observeScreenState()
        handleBackButton()
        handlePlayButton()
        handleLikeButton()
        handleAddToPlaylistButton()
        setOnClickToNavigateAddingPlaylistFragment()
//        initRecycler()


    }

    private fun handleAddToPlaylistButton() {
        binding.fabOpenBottomSheet.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

    private fun setOnClickToNavigateAddingPlaylistFragment() {
        binding.bottomSheetContainer.btAddNewPlaylist.setOnClickListener {
//            viewModel.putItOnBackground()
            findNavController().navigate(
                PlayerFragmentDirections.actionPlayerFragmentToAddPlaylistFragment()
            )
        }
    }

    private fun handleLikeButton() {
        binding.fabAddToFavs.apply {
            setOnClickListener {
                if (Debouncer().playClickDebounce(this, lifecycleScope)) {
                    viewModel.addOrRemoveFromFavorites()
                }
            }
        }
    }

    private fun handlePlayButton() {
        binding.fabPlay.apply {
            setOnClickListener {
                if (Debouncer().playClickDebounce(this, lifecycleScope)) {

                    if (mBound) {

//                        mService.handlePlaying()
                        requireActivity().startService(
                            PlayerService.newIntent(
                                requireContext(),
                                args.track.mapTrackToTrackForPlayer()
                            )
                        )
                        // Call a method from the LocalService.
                        // However, if this call is something that might hang, then put this request
                        // in a separate thread to avoid slowing down the activity performance.
                    }
//                    wM()

//                    context.startForegroundService(MyForegroundService.newIntent(requireContext(), MyForegroundService.STARTFOREGROUND_ACTION, args.track.mapTrackToTrackForPlayer()))
//                    viewModel.playbackControl()
                }
            }
        }
    }

    fun obserePreperingForService(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.preparedForService.collect(){
                if (it == MyMediaPlayer.PlayerState.PREPARED_FOR_SERVICE){
                    mService.handlePlaying()
                }
            }
        }
    }

//    fun wM() {
//        WorkManager.getInstance(requireContext().applicationContext).apply {
//            enqueueUniqueWork(
//                MyForegroundService.WORK_NAME,
//                ExistingWorkPolicy.APPEND,
//                MyForegroundService.makeRequest(page++)
//            )
//        }
//    }




//    fun isJobServiceOn(): Boolean {
//
//        val scheduler = requireContext().getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
//        var hasBeenScheduled = false
//        for (jobInfo in scheduler.allPendingJobs) {
//            if (jobInfo.id == JOB_ID) {
//                hasBeenScheduled = true
//                break
//            }
//        }
//        return hasBeenScheduled
//    }
//    fun jobIt(){
//            val componentName = ComponentName(requireActivity().applicationContext, MyForegroundService::class.java)
//        val intent = MyForegroundService.newBundle(args.track.mapTrackToTrackForPlayer())
//            val jobInfo = JobInfo.Builder(MyForegroundService.JOB_ID, componentName)
//                .setExtras(TRACK, intent)
//                .setRequiresCharging(false)
//                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
//                .build()
//
//            val jobScheduler = requireContext().getSystemService(AppCompatActivity.JOB_SCHEDULER_SERVICE) as JobScheduler
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                jobScheduler.schedule(jobInfo)
//            }



    private fun handleBackButton() {
        binding.btnBack.apply {
            setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }



    private fun observeScreenState(){
      viewLifecycleOwner.lifecycleScope .launch {
          repeatOnLifecycle(Lifecycle.State.RESUMED) {
              viewModel.mergedStates.collect() {
//                  Log.i(TAG, "observeScreenState: $it")
                  when (it){
                      is PlayerScreenState.PlayerError -> {
                          it.render(binding)
                          Toast.makeText(
                              requireContext(),
                              resources.getText(R.string.impossible_to_play),
                              Toast.LENGTH_SHORT
                          ).show()
                      }
                      is PlayerScreenState.UpdatePlaylistList ->{
                          it.update(playlistAdapter)
                      }
                      is PlayerScreenState.AddTrackToPlaylist -> {
                          if ( it.makeToast(requireContext())){
                              bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                          }
                      }
                      else -> {
                          it.render(binding)
                      }
                  }
              }
          }
        }
    }


    private fun parseIntent(trackForAdapter: TrackForAdapter): PlayerTrack {
        return trackForAdapter.mapTrackToTrackForPlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop: ")
        requireActivity().unbindService(connection)
        mBound = false

//        requireContext().startForegroundService(MyForegroundService.newIntent(requireContext(), MyForegroundService.STOPFOREGROUND_ACTION))
        viewModel.onStop()
    }
    override fun onResume() {
        super.onResume()
        viewModel.onResumed()

    }

    override fun onDestroy() {
        super.onDestroy()
//        viewModel.onDestroy()
    }

    private fun initRecycler() {
        playlistAdapter.apply {
            onClickListener = {
                    viewModel.addTrackToPlaylist(
                        _track =  args.track,
                        playlist = it
                    )
            }
            showImage = { view: ImageView, path: String ->
                val file = viewModel.getFile(path, resources.getString(R.string.my_album_name))
                view.loadImage(
                    file,
                    size = resources.getInteger(R.integer.picture_small_size),
                    radius = resources.getDimensionPixelSize(R.dimen.radius_small)
                )
            }
        }
        binding.bottomSheetContainer.rvPlaylists.apply {
            adapter = playlistAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initBottomSheet() {
        bottomSheetBehavior =
            BottomSheetBehavior.from(binding.bottomSheetContainer.bottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset+1f
            }
        })
    }

    companion object{
        private const val TAG = "PlayerFragment"
    }
}