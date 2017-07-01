package com.yingwumeijia.baseywmj.function.casedetails.team

import android.support.v4.app.Fragment
import android.view.View
import android.widget.ImageView
import com.yingwumeijia.baseywmj.AppTypeManager
import com.yingwumeijia.baseywmj.R
import com.yingwumeijia.baseywmj.api.Api
import com.yingwumeijia.baseywmj.function.introduction.company.CompanyActivity
import com.yingwumeijia.baseywmj.function.introduction.employee.EmployeeActivity
import com.yingwumeijia.baseywmj.utils.net.HttpUtil
import com.yingwumeijia.baseywmj.utils.net.subscriber.SimpleSubscriber
import com.yingwumeijia.commonlibrary.utils.ListUtil
import com.yingwumeijia.commonlibrary.utils.adapter.recyclerview.CommonRecyclerAdapter
import com.yingwumeijia.commonlibrary.utils.adapter.recyclerview.RecyclerViewHolder
import com.yingwumeijia.commonlibrary.utils.glide.JImageLolder
import rx.Observable

/**
 * Created by jamisonline on 2017/6/27.
 */
class CaseTeamPresenter(var fragment: Fragment, var caseId: Int, var view: CaseTeamContract.View) : CaseTeamContract.Presenter {

    var isAppc = AppTypeManager.isAppC()

    var teamData: ProductionTeamBean? = null

    val teamAdapter by lazy {
        createTeamAdapter()
    }

    val ceremonyAdapter by lazy {
        createCeremonyAdapter()
    }

    /**
     * 创建 团队列表适配器
     */
    private fun createTeamAdapter(): CommonRecyclerAdapter<ProductionTeamBean.EmployeesBean> {
        var companyDecorateTypes = ""
        if (teamData!!.company.decorateTypes != null)
            for (i in 0..teamData!!.company.decorateTypes.size - 1) {

                val s: String
                if (i == teamData!!.company.decorateTypes.size - 1) {
                    s = teamData!!.company.decorateTypes[i]
                } else {
                    s = teamData!!.company.decorateTypes[i] + "  |  "
                }
                companyDecorateTypes += s
            }

        return object : CommonRecyclerAdapter<ProductionTeamBean.EmployeesBean>(null, fragment, teamData!!.employees as ArrayList<ProductionTeamBean.EmployeesBean>, R.layout.item_production_team) {
            override fun convert(holder: RecyclerViewHolder, t: ProductionTeamBean.EmployeesBean, position: Int) {
                val isLast: Boolean = position == getItemCount() - 1
                val companyBean = teamData!!.company

                holder.run {
                    setVisible(R.id.topPadding, false)
                    setVisible(R.id.line, !isLast)
                    if (isLast) {
                        JImageLolder.loadPortrait100(fragment!!, holder.getViewWith(R.id.iv_portrait) as ImageView, companyBean.companyHeadImage)
                        setTextWith(R.id.tv_name, companyBean.companyName)
                        setTextWith(R.id.tv_job, companyDecorateTypes)

                    } else {
                        JImageLolder.loadPortrait100(fragment!!, holder.getViewWith(R.id.iv_portrait) as ImageView, t.headImage)
                        setTextWith(R.id.tv_name, t.name)
                        setTextWith(R.id.tv_job, t.employeeDetailType)
                    }
                    setOnItemClickListener(object : RecyclerViewHolder.OnItemCliceListener {
                        override fun itemClick(itemView: View, position: Int) {
                            if (isLast)
                                CompanyActivity.start(mContext!!, companyBean.companyId, caseId)
                            else
                                EmployeeActivity.start(mContext!!, t.userId, caseId)
                        }
                    })
                }
            }

        }
    }


    /**
     * 创建竣工图片适配器
     */
    private fun createCeremonyAdapter(): CommonRecyclerAdapter<ProductionTeamBean.SurroundingMaterials.CeremonyBean> {
        return object : CommonRecyclerAdapter<ProductionTeamBean.SurroundingMaterials.CeremonyBean>(null, fragment, teamData!!.surroundingMaterials as ArrayList<ProductionTeamBean.SurroundingMaterials.CeremonyBean>, R.layout.item_ceremony) {
            override fun convert(holder: RecyclerViewHolder, ceremonyBean: ProductionTeamBean.SurroundingMaterials.CeremonyBean, position: Int) {
                holder
                        .run {
                            setVisible(R.id.bottomPadding, position == itemCount - 1)
                            setVisible(R.id.line, position == 0)
                            setTextWith(R.id.tv_title, ceremonyBean.title)
                            setImageUrl480(fragment!!, R.id.iv_img, ceremonyBean.pics[0])
                        }
            }
        }
    }

    override fun start() {
        var ob: Observable<ProductionTeamBean>? = null
        if (isAppc) ob = Api.service.getProductionTeamData_C(caseId)
        else Api.service.getProductionTeamData_E(caseId)

        HttpUtil.getInstance().toNolifeSubscribe(ob, object : SimpleSubscriber<ProductionTeamBean>(fragment.context) {
            override fun _onNext(t: ProductionTeamBean?) {
                if (t != null) {
                    teamData = t
                    view.showTeamList(t)

                    val ceremonyBeanList by lazy { ArrayList<ProductionTeamBean.SurroundingMaterials.CeremonyBean>(); }
                    if (t.surroundingMaterials == null) return
                    if (t.surroundingMaterials.startCeremony != null)
                        ceremonyBeanList.add(t.surroundingMaterials.startCeremony)
                    if (t.surroundingMaterials.endCeremony != null)
                        ceremonyBeanList.add(t.surroundingMaterials.endCeremony)
                    if (!ListUtil.isEmpty(ceremonyBeanList))
                        view.showCeremonyPic(ceremonyBeanList)
                }
            }

        })
    }

}