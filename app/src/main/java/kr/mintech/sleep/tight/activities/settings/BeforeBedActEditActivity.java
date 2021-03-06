package kr.mintech.sleep.tight.activities.settings;

import kr.mintech.sleep.tight.R;
import kr.mintech.sleep.tight.consts.NumberConst;
import kr.mintech.sleep.tight.controllers.settings.BeforeBedActAdapter;
import kr.mintech.sleep.tight.controllers.settings.BeforeBedActController;
import kr.mintech.sleep.tight.listeners.OnRequestEndListener;
import Util.Logg;
import Util.PopupUtil;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class BeforeBedActEditActivity extends Activity
{
	private ListView _itemList;
	private Button _btnAddItem, _btnOkay;
	
	private BeforeBedActAdapter _adapter;
	private BeforeBedActController _controller;
	
	private int _deleteId;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_before_bed_edit);
		
		_adapter = new BeforeBedActAdapter(this);
		_controller = new BeforeBedActController(onRequestEndListener);
		
		_itemList = (ListView) findViewById(R.id.listview_item);
		_btnAddItem = (Button) findViewById(R.id.btn_add_new_item);
		_btnOkay = (Button) findViewById(R.id.btn_ok);
		
		_btnAddItem.setOnClickListener(addItemClickListener);
		_btnOkay.setOnClickListener(endClickListener);
		
		_itemList.setOnItemClickListener(itemClickListener);
		_itemList.setAdapter(_adapter);
		
		_controller.requestBeforeBedAct();
	}
	
	
	@Override
	public void onResume()
	{
		super.onResume();
		_controller.requestBeforeBedAct();
	}
	
	
	private void addItems()
	{
		_adapter.putItems(_controller._units);
	}
	
	/*
	 * Listener
	 */
	private OnItemClickListener itemClickListener = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> adapterView, View clickedView, int pos, long $id)
		{
			//Delete
			_deleteId = (int) $id;
			PopupUtil.showMessageDialog(BeforeBedActEditActivity.this, "Delete", "Do you want to delete " + _controller._units.get(pos).name + "?", deleteClickListener, "Delete", null, "Cancel");
			
		}
	};
	
	private android.content.DialogInterface.OnClickListener deleteClickListener = new android.content.DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			_controller.requestRemoveBeforeBedAct(_deleteId);
		}
	};
	/*
	 * Network Listener
	 */
	private OnRequestEndListener onRequestEndListener = new OnRequestEndListener()
	{
		@Override
		public void onRequestEnded(int $tag, Object $object)
		{
			switch ($tag)
			{
				case NumberConst.requestEndAddAction:
					_controller.requestBeforeBedAct();
					break;
				case NumberConst.requestEndBeforeBedAct:
					Logg.w("AddActivityView | enclosing_method()", "Sleep Disturbances: successfully loaded");
					addItems();
					break;
				case NumberConst.requestFail:
					Logg.w("AddActivityView | enclosing_method()", "Network Failure");
					Toast.makeText(BeforeBedActEditActivity.this, "Network status is bad", Toast.LENGTH_SHORT).show();
					break;
			}
		}
		
		
		@Override
		public void onRequest(Object $unit)
		{
		}
		
		
		@Override
		public void onRequestError(int $tag, String $errorStr)
		{
		}
	};
	private OnClickListener addItemClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			//Add Item 
			Intent kIntent = new Intent(BeforeBedActEditActivity.this, AddBeforeBedActActivity.class);
			startActivity(kIntent);
		}
	};
	
	private OnClickListener endClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			finish();
		}
	};
}
