package com.tch.kuwanx.module;

import com.tch.kuwanx.plugin.RepaymentPlugin;
import com.tch.kuwanx.plugin.UnRepaymentPlugin;

import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imkit.widget.provider.FilePlugin;
import io.rong.imlib.model.Conversation;

/**
 * Created by jiaop on 2017/11/22.
 * 聊天页面底部加号里面的按钮
 */

public class SampleExtensionModule extends DefaultExtensionModule {

    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules = super.getPluginModules(conversationType);
        pluginModules.add(new RepaymentPlugin());
        pluginModules.add(new UnRepaymentPlugin());
        return pluginModules;
    }
}
