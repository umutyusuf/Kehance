package com.cutta.kehance.ui.detail

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.cutta.kehance.R
import com.cutta.kehance.data.remote.model.CommentItem
import com.cutta.kehance.data.remote.model.ModulesItem
import com.cutta.kehance.data.remote.model.Project
import com.cutta.kehance.data.remote.model.ProjectDetail
import com.cutta.kehance.ui.base.BaseActivity
import com.cutta.kehance.util.ModuleType
import com.cutta.kehance.util.TransformationType
import com.cutta.kehance.util.extension.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.item_comment.view.*
import kotlinx.android.synthetic.main.layout_detail_header.*


/**
 * Created by CuneytCarikci on 13/01/2018.
 */

class DetailActivity : BaseActivity<DetailViewModel>(), CommentListAdapter.CommentClickListener {

    companion object {
        private const val INTENT_EXTRA_PROJECT_ID = "DetailActivity:Id"
        private const val INTENT_EXTRA_PROJECT_NAME = "DetailActivity:Name"

        @JvmStatic
        fun getIntent(context: Context, projectId: Int, projectName: String): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PROJECT_ID, projectId)
            intent.putExtra(INTENT_EXTRA_PROJECT_NAME, projectName)
            return intent
        }
    }

    private lateinit var project: Project
    private lateinit var commentsAdapter: CommentListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolBar()
        setRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.setProjectId(intent.getIntExtra(INTENT_EXTRA_PROJECT_ID, -1))
        viewModel.details.observe(this, Observer { setViews(it) })
        viewModel.comments.observe(this, Observer {
            val comments = it?.comments
            comments?.let { commentsAdapter.update(it) }
        })
    }

    private fun initViews(modules: List<ModulesItem>?) {
        setContentModules(modules)
        appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val percentage = Math.abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange
            toolbarTitle.alpha = percentage
        }
    }


    private fun setRecyclerView() {
        commentsAdapter = CommentListAdapter(layoutResId = R.layout.item_comment, listener = this) {
            with(it) {
                itemUserImage.load(user.images?.jsonMember100, TransformationType.CIRCLE)
                itemUserName.text = user.displayName
                itemCommentDate.text = createdOn.toString()
                itemComment.text = comment
            }
        }

        with(commentsRecyclerView) {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            setHasFixedSize(true)
            adapter = commentsAdapter
            isNestedScrollingEnabled = false
            scheduleLayoutAnimation()
        }
    }

    private fun setViews(projectDetail: ProjectDetail?) {
        projectDetail?.project?.let {
            project = it
            projectName.text = it.name
            projectFields.text = it.fields.joinToString()
            likeCount.text = it.stats.appreciations.toString()
            viewsCount.text = it.stats.views.toString()
            commentCount.text = it.stats.comments.toString()
            ownerImage.load(it.owners.getOrNull(0)?.images?.jsonMember100 ?: "", TransformationType.CIRCLE)
            ownerName.text = it.owners.getOrNull(0)?.displayName ?: ""
            initViews(it.modules)
        }
    }

    private fun setToolBar() {
        setSupportActionBar(toolbar)
        toolbarTitle.text = intent.getStringExtra(INTENT_EXTRA_PROJECT_NAME)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setContentModules(modules: List<ModulesItem>?) {//will be improved

        modules?.let {
            for (module in modules) {
                var tempView: View
                when (module.type) {
                    ModuleType.TEXT.type -> {
                        tempView = TextView(this)
                        tempView.setTextFromHtml(module.text)
                    }
                    ModuleType.IMAGE.type -> {
                        tempView = ImageView(this)
                        tempView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)
                        tempView.load(module)
                    }
                    ModuleType.EMBED.type -> {
                        tempView = TextView(this)
                        tempView.setTextFromHtml(module.embed)
                    }
                    ModuleType.MEDIA_COLLECTION.type -> {
                        tempView = TextView(this)
                        tempView.text = "MEDIA_COLLECTION"
                    }
                    else -> {
                        tempView = TextView(this)
                        tempView.text = "UNKNOWN_TYPE"
                    }
                }
                nestedLinearLayout.addView(tempView)

            }
        }
    }

    override fun onCommentClick(item: CommentItem) {
        Toast.makeText(this, item.id.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun getLayoutId(): Int = R.layout.activity_detail

    override fun getViewModel(): Class<DetailViewModel> = DetailViewModel::class.java

}