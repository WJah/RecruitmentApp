//package com.recruitmentclient.activity;
//
//import android.app.ListFragment;
//
//public class BasicListFragment extends ListFragment
//{
//
//	/**
//	 * 新闻分类页面
//	 * 
//	 * @author chen
//	 * @date 2012-12-18 下午5:48:02
//	 */
//		private String url;
//		/** ListView */
//		private PullListView mListView;
//		// /** 分页 */
//		// private Page page = new Page();
//		/** 商铺适配器 */
//		private Adapter mAdapter = new Adapter(activity);;
//		/** 集合数据 */
//		public ArrayList<News> mListData;
//		/** 是否重新加载 */
//		private boolean isClear;
//
//		private LayoutInflater mInflater;
//		/** 新闻详细页面 url */
//		private String detailUrl = C.http.http_news_detail;
//
//		public NewsFragment()
//		{
//		}
//
//		public NewsFragment(Context context, String url)
//		{
//			this.context = context;
//			this.url = url;
//			mListData = null;
//		}
//
//		@Override
//		public void onCreate(Bundle savedInstanceState)
//		{
//			super.onCreate(savedInstanceState);
//			Log.i(tag, "onCreate");
//		}
//
//		// 加载多次
//		public void onActivityCreated(Bundle savedInstanceState)
//		{
//			super.onActivityCreated(savedInstanceState);
//			Log.i(tag, "onActivityCreated");
//			mListView = (PullListView) getListView();// (ListView)
//														// activity.findViewById(android.R.id.list);
//			if (mListData != null && mListData.size() > 0)
//			{
//				return;
//			}
//
//			mListData = new ArrayList<News>();
//			setListAdapter(mAdapter);
//			mListView.onRefreshComplete();
//			mListView.setonRefreshListener(new OnRefreshListener()
//			{
//				public void onRefresh()
//				{
//					isClear = true;
//					// 初始化数据
//					AnsynHttpRequest.requestByGet(context, callbackData,
//							R.string.http_news, url, true, true, true);
//				}
//			});
//			// 初始化数据
//			AnsynHttpRequest.requestByGet(context, callbackData,
//					R.string.http_news, url, true, true, false);
//		}
//
//		// 加载多次
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState)
//		{
//			super.onCreateView(inflater, container, savedInstanceState);
//			View view = inflater.inflate(R.layout.fragment_news_or_hotline,
//					container, false);
//			mInflater = inflater;
//			Log.i(tag, "onCreateView");
//			return view;
//		}
//
//		@Override
//		public void onStart()
//		{
//			Log.i(tag, "onStart");
//			super.onStart();
//		}
//
//		@Override
//		public void onStop()
//		{
//			super.onStop();
//			Log.i(tag, "onStop");
//		}
//
//		@Override
//		public void onDestroy()
//		{
//			super.onDestroy();
//			Log.i(tag, "onDestroy");
//		}
//
//		/**
//		 * 异步回调回来并处理数据
//		 */
//		private ObserverCallBack callbackData = new ObserverCallBack()
//		{
//			public void back(String data, int url)
//			{
//				Message msg = new Message();
//				switch (url)
//				{
//				case R.string.http_news: // 解析主题信息数据
//					msg = new Message();
//					if (data == null)
//					{
//						msg.what = 2;
//						mHandler.sendMessage(msg);
//						return;
//					}
//					try
//					{
//						News news = new News();
//						ArrayList<News> dataInfo = news.getListInfo(data);
//						if (isClear)
//						{
//							if (dataInfo != null && dataInfo.size() > 0)
//								mListData.clear();
//							isClear = false;
//						}
//
//						if (mListData != null && mListData.size() > 0)
//							msg.what = 1;
//						else
//							msg.what = 0;
//						if (dataInfo == null || dataInfo.size() == 0)
//						{
//							if (mListData != null && mListData.size() > 0)
//							{
//								msg = new Message();
//								msg.what = 2;
//								mHandler.sendMessage(msg);
//								return;
//							}
//						} else
//						{
//							mListData.addAll(dataInfo);
//						}
//						mHandler.sendMessage(msg);
//					} catch (Exception e)
//					{
//						e.printStackTrace();
//					}
//
//					break;
//				case R.string.http_news_detail:
//					msg = new Message();
//					if (data == null)
//					{
//						msg.what = 2;
//						mHandler.sendMessage(msg);
//						return;
//					}
//					try
//					{
//						NewsDetail newsDetail = new NewsDetail();
//						newsDetail = newsDetail.getInfo(data, detailUrl);
//						msg.what = 3;
//						msg.obj = newsDetail;
//						if (newsDetail.content == null
//								|| newsDetail.content.length() == 0)
//						{
//							msg = new Message();
//							msg.what = 2;
//							mHandler.sendMessage(msg);
//							return;
//						}
//						mHandler.sendMessage(msg);
//					} catch (Exception e)
//					{
//						msg = new Message();
//						msg.what = 2;
//						mHandler.sendMessage(msg);
//					}
//					break;
//				default:
//					break;
//				}
//			};
//		};
//
//		private Handler mHandler = new Handler()
//		{
//			@Override
//			public void handleMessage(Message msg)
//			{
//				super.handleMessage(msg);
//				switch (msg.what)
//				{
//				case 0:
//					mAdapter.notifyDataSetChanged();
//					mListView.onRefreshComplete();
//					// if(mListData.size() == 0){
//					// // Toast.makeText(context, R.string.dialog_title_nowData,
//					// Toast.LENGTH_LONG).show();
//					// mListView.setVisibility(View.GONE);
//					// } else mListView.setVisibility(View.VISIBLE);
//					break;
//				case 1:
//					mAdapter.notifyDataSetChanged();
//					// if(mListData.size() == 0){
//					// // Toast.makeText(context, R.string.dialog_title_nowData,
//					// Toast.LENGTH_LONG).show();
//					// mListView.setVisibility(View.GONE);
//					// }else mListView.setVisibility(View.VISIBLE);
//					break;
//				case 2:
//					Toast.makeText(context, R.string.no_data, Toast.LENGTH_LONG)
//							.show();
//					break;
//				case 3:
//					NewsDetail newsDetail = (NewsDetail) msg.obj;
//					Intent intent = new Intent(context,
//							NewsDetailActivity.class);
//					intent.putExtra("content", newsDetail.content);
//					intent.putExtra("hostUrl", newsDetail.hostUrl);
//					startActivity(intent);
//					break;
//				default:
//					break;
//				}
//			}
//		};
//
//		public final class ViewHolder
//		{
//			public TextView title;
//			public TextView time;
//		}
//
//		// 添加列表内容
//		public class Adapter extends BaseAdapter
//		{
//			public Adapter()
//			{
//			}
//
//			@Override
//			public boolean areAllItemsEnabled()
//			{
//				return super.areAllItemsEnabled();
//			}
//
//			public Adapter(Context context)
//			{
//			}
//
//			@Override
//			public int getCount()
//			{
//				return mListData.size();
//			}
//
//			@Override
//			public Object getItem(int position)
//			{
//				return null;
//			}
//
//			@Override
//			public long getItemId(int position)
//			{
//				return position;
//			}
//
//			@Override
//			public View getView(final int position, View convertView,
//					ViewGroup parent)
//			{
//				final News news = mListData.get(position);
//				ViewHolder holder = null;
//				if (convertView == null)
//				{
//					holder = new ViewHolder();
//					convertView = mInflater.inflate(R.layout.list_item_mian,
//							null);
//					holder.title = (TextView) convertView
//							.findViewById(R.id.list_main_txt_title);
//					holder.time = (TextView) convertView
//							.findViewById(R.id.list_main_txt_time);
//					convertView.setTag(holder);
//				} else
//				{
//					holder = (ViewHolder) convertView.getTag();
//				}
//
//				// 进行数据设置
//				holder.title.setText(news.title);
//				holder.time.setText(news.time);
//				convertView.setOnClickListener(new OnClickListener()
//				{
//					@Override
//					public void onClick(View v)
//					{ // 加载详细新闻
//						detailUrl = mListData.get(position).childUrl;
//						AnsynHttpRequest.requestByGet(context, callbackData,
//								R.string.http_news_detail, detailUrl, true,
//								true, false);
//					}
//				});
//				return convertView;
//			}
//		}
//	}
//
//}
