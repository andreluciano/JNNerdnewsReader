package br.com.andreluciano.jnnerdnewsreader.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.andreluciano.jnnerdnewsreader.app.R;
import br.com.andreluciano.jnnerdnewsreader.app.bean.FeedMessage;

/**
 * Created by André on 02/06/2014.
 */
public class FeedMessagesAdapter extends RecyclerView.Adapter<FeedMessagesAdapter.ViewHolder> {

    private List<FeedMessage> feedList;
    private Context context;
    OnItemClickListener mItemClickListener;
    private int lastPosition = -1;

    public FeedMessagesAdapter(Context context, List<FeedMessage> feedList) {
        this.feedList = feedList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textTitle;
        private ImageView imageTitulo;
        private LinearLayout linearRoot;

        public ViewHolder(View v) {
            super(v);
            textTitle = (TextView) v.findViewById(R.id.textTitle);
            imageTitulo = (ImageView) v.findViewById(R.id.imageTitulo);
            linearRoot = (LinearLayout) v.findViewById(R.id.linearRoot);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_messages_adapter, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FeedMessage feedMessage = feedList.get(position);

        holder.textTitle.setText(feedMessage.getTitle());

        String url = getImagem(this.feedList.get(position).getContentEncoded());
        Picasso.with(context)
                .load(url)
                .into(holder.imageTitulo);

        setAnimation(holder.linearRoot, position);
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public FeedMessage getItem(int position) {
        return feedList.get(position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.animacao_lista_top);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public String getImagem(String html) {
        String imagem = "";
        Pattern pattern = Pattern.compile("<img(.+?)/>");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            imagem = matcher.group();
        }

        imagem = imagem.split("src\\s*=\"")[1].split("\\s*\">")[0];

        return imagem;
    }

    public interface OnItemClickListener {
        public void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}