package cn.x5456.summer.web.servlet.config.annotation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class WebMvcConfigurerComposite implements WebMvcConfigurer {

	private final List<WebMvcConfigurer> delegates = new ArrayList<>();

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		for (WebMvcConfigurer mvcConfigurer : delegates) {
			mvcConfigurer.addInterceptors(registry);
		}
	}

	public void addWebMvcConfigurers(Collection<WebMvcConfigurer> configurers) {
		delegates.addAll(configurers);
	}
}