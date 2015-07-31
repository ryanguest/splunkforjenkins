package com.splunk.splunkjenkins;

import com.splunk.splunkjenkins.Messages;
import hudson.Extension;
import hudson.tools.ToolDescriptor;
import hudson.tools.ToolInstallation;
import hudson.tools.ToolProperty;
import hudson.util.FormValidation;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import java.util.List;

public class SplunkJenkinsInstallation extends ToolInstallation {

    @DataBoundConstructor
    public SplunkJenkinsInstallation(String name, String home, List<? extends ToolProperty<?>> properties) {
        super(name, home, properties);
    }

    public static Descriptor getSplunkDescriptor() {
        return (Descriptor) Jenkins.getInstance().getDescriptor(SplunkJenkinsInstallation.class);
    }

    @Extension
    public static final class Descriptor extends ToolDescriptor<SplunkJenkinsInstallation> {
        public String globalConfigTitle = Messages.GlobalConfigTitle();

        // Defaults plugin global config values:
        public String host;
        public Integer managementPort = 8089;
        public String username = "admin";
        public String password;
        public String scheme;
        public Integer httpInputPort = 8088;
        public long maxEventsBatchCount = 3;
        public long maxEventsBatchSize = Long.MAX_VALUE;
        public long retriesOnError = 3;
        public String sendMode;
        public long delay = 0;
        public String indexName = "main";
        public String source = null;

        public Descriptor() {
            super();
            load();
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            req.bindJSON(this, formData.getJSONObject("splunkjenkins"));
            save();
            return super.configure(req, formData);
        }

        @Override
        public ToolInstallation newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            req.bindJSON(this, formData.getJSONObject("splunkjenkins"));
            save();
            return super.newInstance(req, formData);
        }

        @Override
        public String getDisplayName() {
            return Messages.GlobalConfigTitle();
        }

        /*
         * Form validation methods
         */
        public FormValidation doCheckInteger(@QueryParameter("value") String value) {
            try {
                Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return FormValidation.error(Messages.ValueIntErrorMsg());
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckLong(@QueryParameter("value") String value) {
            try {
                Long.parseLong(value);
            } catch (NumberFormatException e) {
                return FormValidation.error(Messages.ValueIntErrorMsg());
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckHost(@QueryParameter("value") String value) {
            if (StringUtils.isBlank(value)) {
                return FormValidation.warning(Messages.PleaseProvideHost());
            }

            return FormValidation.ok();
        }

        public FormValidation doCheckString(@QueryParameter("value") String value) {
            if (StringUtils.isBlank(value)) {
                return FormValidation.error(Messages.ValueCannotBeBlank());
            }

            return FormValidation.ok();
        }
    }
}