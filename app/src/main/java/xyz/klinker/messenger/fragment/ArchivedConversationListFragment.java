package xyz.klinker.messenger.fragment;

import android.support.design.widget.NavigationView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.List;

import xyz.klinker.messenger.R;
import xyz.klinker.messenger.activity.MessengerActivity;
import xyz.klinker.messenger.adapter.ConversationListAdapter;
import xyz.klinker.messenger.adapter.view_holder.ConversationViewHolder;
import xyz.klinker.messenger.shared.data.DataSource;
import xyz.klinker.messenger.shared.data.model.Conversation;
import xyz.klinker.messenger.utils.swipe_to_dismiss.SwipeTouchHelper;
import xyz.klinker.messenger.utils.swipe_to_dismiss.UnarchiveSwipeSimpleCallback;

public class ArchivedConversationListFragment extends ConversationListFragment {

    // only grab the archived messages
    @Override
    protected List<Conversation> getCursor(DataSource source) {
        return source.getArchivedConversationsAsList(getActivity());
    }

    // create swipe helper that has the unarchive icon instead of the archive one
    @Override
    public ItemTouchHelper getSwipeTouchHelper(ConversationListAdapter adapter) {
        return new SwipeTouchHelper(new UnarchiveSwipeSimpleCallback(adapter));
    }

    // change the text to "1 conversation moved to the inbox
    @Override
    protected String getArchiveSnackbarText() {
        return getResources().getQuantityString(R.plurals.conversations_unarchived,
                pendingArchive.size(), pendingArchive.size());
    }

    // unarchive instead of archive when the snackbar is dismissed
    @Override
    protected void performArchiveOperation(DataSource dataSource, Conversation conversation) {
        dataSource.unarchiveConversation(getActivity(), conversation.id);
    }

    // always consume the back event and send us to the conversation list
    @Override
    public boolean onBackPressed() {
        if (!super.onBackPressed()) {
            NavigationView navView = (NavigationView) getActivity().findViewById(R.id.navigation_view);
            navView.getMenu().getItem(1).setChecked(true);

            getActivity().setTitle(getString(R.string.app_title));
            ((MessengerActivity) getActivity()).displayConversations();
        }

        return true;
    }
    
    @Override
    public void onConversationContracted(ConversationViewHolder viewHolder) {
        super.onConversationContracted(viewHolder);

        NavigationView navView = (NavigationView) getActivity().findViewById(R.id.navigation_view);
        navView.getMenu().getItem(2).setChecked(true);
    }

}
