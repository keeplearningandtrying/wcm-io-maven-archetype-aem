#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.testcontext;

#if ( $optionContextAwareConfig == "y" )
import static io.wcm.testing.mock.wcmio.caconfig.ContextPlugins.WCMIO_CACONFIG;
#end
#if ( $optionWcmioHandler == "y" )
import static io.wcm.testing.mock.wcmio.handler.ContextPlugins.WCMIO_HANDLER;
import static io.wcm.testing.mock.wcmio.sling.ContextPlugins.WCMIO_SLING;
#end
#if ( $optionContextAwareConfig == "y" )
import static org.apache.sling.testing.mock.caconfig.ContextPlugins.CACONFIG;
#end

import java.io.IOException;

import org.apache.sling.api.resource.PersistenceException;
import org.jetbrains.annotations.NotNull;

#if ( $optionWcmioHandler == "y" )
import io.wcm.handler.media.spi.MediaFormatProvider;
#end
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextCallback;
#if ( $optionContextAwareConfig == "y" )
import io.wcm.testing.mock.wcmio.caconfig.MockCAConfig;
#end

#if ( $optionWcmioHandler == "y" )
import ${package}.config.AppTemplate;
import ${package}.config.impl.LinkHandlerConfigImpl;
import ${package}.config.impl.MediaFormatProviderImpl;

#end
/**
 * Sets up {@link AemContext} for unit tests in this application.
 */
public final class AppAemContext {

  private AppAemContext() {
    // static methods only
  }

  public static AemContext newAemContext() {
    return new AemContextBuilder()
#if ( $optionContextAwareConfig == "y" )
        .plugin(CACONFIG)
#end
#if ( $optionWcmioHandler == "y" )
        .plugin(WCMIO_SLING, WCMIO_CACONFIG, WCMIO_HANDLER)
#elseif ( $optionContextAwareConfig == "y" )
        .plugin(WCMIO_CACONFIG)
#end
        .afterSetUp(SETUP_CALLBACK)
        .build();
  }

  /**
   * Custom set up rules required in all unit tests.
   */
  private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {
    @Override
    public void execute(@NotNull AemContext context) throws PersistenceException, IOException {

#if ( $optionContextAwareConfig == "y" || $optionWcmioHandler == "y" )
      // context path strategy
#if ( $optionWcmioHandler == "y" )
      MockCAConfig.contextPathStrategyRootTemplate(context, AppTemplate.EDITORIAL_HOMEPAGE.getTemplatePath());
#else
      MockCAConfig.contextPathStrategyRootTemplate(context, "/apps/${projectName}#if($optionMultiBundleLayout=='y')/core#end/templates/homepage");
#end

#end
#if ( $optionWcmioHandler == "y" )
      // setup handler
      context.registerInjectActivateService(new LinkHandlerConfigImpl());
      context.registerService(MediaFormatProvider.class, new MediaFormatProviderImpl());

#end
      // register sling models
      context.addModelsForPackage("${package}");

    }
  };

}
