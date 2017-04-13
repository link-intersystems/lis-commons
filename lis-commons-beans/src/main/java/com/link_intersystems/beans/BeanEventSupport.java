package com.link_intersystems.beans;

import com.link_intersystems.lang.Assert;

public class BeanEventSupport<B, T> {

	private T listener;

	private Bean<B> bean;

	public BeanEventSupport() {
	}

	public BeanEventSupport(T listener) {
		Assert.notNull("listener", listener);
		this.listener = listener;
	}

	public void setListener(T listener) {
		T actualListener = this.listener;
		Bean<B> actualBean = this.bean;

		if (actualListener != null && actualBean != null) {
			removeListener(actualBean, actualListener);
		}

		this.listener = listener;

		actualListener = this.listener;

		if (actualListener != null && actualBean != null)
			addListener(actualBean, actualListener);
	}

	public T getListener() {
		return listener;
	}

	public void setBean(B bean) {
		Bean<B> actualBean = this.bean;
		if (actualBean != null && actualBean.getTarget() == bean) {
			return;
		}

		T actualListener = this.listener;

		if (actualBean != null && actualListener != null) {
			removeListener(actualBean, actualListener);
		}

		if (bean == null) {
			this.bean = null;
		} else {
			this.bean = new Bean<B>(bean);
		}

		actualBean = this.bean;

		if (actualBean != null && actualListener != null)

		{
			addListener(actualBean, actualListener);
		}
	}

	private void addListener(Bean<B> actualBean, T actualListener) {
		actualBean.addListener(actualListener);
	}

	private void removeListener(Bean<B> actualBean, T actualListener) {
		actualBean.removeListener(actualListener);
	}

	public B getBean() {
		if (this.bean == null) {
			return null;
		}
		return this.bean.getTarget();
	}

}
