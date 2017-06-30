package ru.org.adons.cblock.ui.view.add

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_add.*
import ru.org.adons.cblock.R
import ru.org.adons.cblock.ext.initList
import ru.org.adons.cblock.ui.activity.BaseFragment
import ru.org.adons.cblock.ui.activity.IMainListener
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

/**
 * Add Phone View, show list of incoming or missed calls
 */
class AddFragment : BaseFragment<IMainListener>() {

    companion object {
        val ADD_FRAGMENT_TAG = "ADD_FRAGMENT_TAG"
    }

    private val adapter: CallLogAdapter = CallLogAdapter()

    @Inject lateinit var addViewModel: AddViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setListener<Activity>(activity, IMainListener::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener?.mainComponent?.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listCallLog.initList(adapter)
        buttonClose.setOnClickListener { listener?.onBackPressed() }
        buttonDone.setOnClickListener {
            addViewModel.addPhones(adapter.getItems())
            listener?.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        getCallLogList()
    }

    // get list of incoming or missed calls
    private fun getCallLogList() {
        addViewModel.getCallLogList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .doOnSubscribe { listener?.showProgress() }
                .doOnNext { listener?.hideProgress() }
                .subscribe(adapter::setItems, this::onError)
    }

}
