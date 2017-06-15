package com.yingwumeijia.baseywmj.function.personal

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yingwumeijia.baseywmj.R
import com.yingwumeijia.baseywmj.base.JBaseFragment
import com.yingwumeijia.baseywmj.function.UserManager
import kotlinx.android.synthetic.main.person_frag.*

/**
 * Created by jamisonline on 2017/5/31.
 */

enum class MenuAction {
    order, //订单
    production, //作品
    bill, //账单
    favourable, //优惠
    collect, //收藏
    twitter, //推客
    apply, //申请入驻
    advice, //我的建议
    beginner, //新手引导
    testH5, //测试H5入口
    history, //历史浏览
    invite//邀请
}

class PersonalFragment : JBaseFragment(), PersonContract.View, PersonGroupMenuAdapter.MenuOnItemClickListener, PersonGroupMenuAdapter.MenuOnItemLongClickListener {


    val presenter: PersonContract.Presenter by lazy {
        PersonPresenter(this, this, lifecycleSubject)
    }

    val personGroupMenuAdapter: PersonGroupMenuAdapter by lazy {
        PersonGroupMenuAdapter(activity, this, this)
    }

    override fun itemClick(action: MenuAction) {
        when (action) {
            MenuAction.order -> ""
        }
    }

    override fun itemLongClick(action: MenuAction) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLogIn(logIn: Boolean) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun showMenus(menuInfosList: ArrayList<ArrayList<MenuInfo>>) {
        personGroupMenuAdapter.refreshMenu(menuInfosList)
    }

    companion object {
        fun newInstance(): PersonalFragment {
            return PersonalFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.person_frag, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.initMenu()
        rv_menu.run {
            layoutManager = LinearLayoutManager(context)
            adapter = personGroupMenuAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        showLogIn(UserManager.isLogin(activity))
        if (UserManager.isLogin(activity))
            presenter.initPersonInfo()
    }
}