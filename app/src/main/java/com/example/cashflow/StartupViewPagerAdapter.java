package com.example.cashflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.cashflow.R;

import java.util.List;

public class StartupViewPagerAdapter extends PagerAdapter {

    Context mContext;
    List<StartupScreenItem> mListScreen;

    public StartupViewPagerAdapter(Context mContext, List<StartupScreenItem> mListScreen) {
        this.mContext = mContext;
        this.mListScreen = mListScreen;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.startup_screen, null);

        ImageView img = layout.findViewById(R.id.intro_img);
        TextView title = layout.findViewById(R.id.intro_text);
        TextView description = layout.findViewById(R.id.intro_description);

         title.setText(mListScreen.get(position).getTitle());
         description.setText(mListScreen.get(position).getDescription());
        img.setImageResource(mListScreen.get(position).getImg());

        container.addView(layout);

        return layout;
    }

    @Override
    public int getCount() {
        return mListScreen.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
