package sk.zpn.zaklad.view;


import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import sk.zpn.zaklad.Html;

public abstract class  MojView  extends VerticalLayout implements View {
        protected Label lblTitle = null;
        private boolean initialized;
        Navigator n;
        public MojView() {
                setSizeFull();
                setSpacing(true);
                addStyleName("aaaa");

        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
                if (!initialized) {
                        init();
                        initialized = true;
                }
        }

        protected abstract void init();
        protected void setTitle(String title) {
                setTitle(title, null);
        }

        protected void setTitle(String title, String subtitle) {
                setTitle(title, subtitle, "#2196F3");
        }
        protected void setTitle(String title, String subtitle, String color) {
                if (lblTitle == null) {
                        lblTitle = new Label("", ContentMode.HTML);
                        addComponent(lblTitle);
                }

                String value = Html.span(title, "font-size:x-large; color:" + color + ";");
                if (subtitle != null)
                        value += "<br>" + Html.small(subtitle);

                lblTitle.setValue(value);
        }


}
