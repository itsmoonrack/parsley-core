package org.spicefactory.parsley;

import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.ConstructorBinding;
import com.google.inject.spi.ConvertedConstantBinding;
import com.google.inject.spi.ExposedBinding;
import com.google.inject.spi.InstanceBinding;
import com.google.inject.spi.LinkedKeyBinding;
import com.google.inject.spi.ProviderBinding;
import com.google.inject.spi.ProviderInstanceBinding;
import com.google.inject.spi.ProviderKeyBinding;
import com.google.inject.spi.UntargettedBinding;

public class ParsleyModuleTargetVisitor<T, V> implements BindingTargetVisitor<T, V> {

	@Override
	public V visit(InstanceBinding<? extends T> binding) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V visit(ProviderInstanceBinding<? extends T> binding) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V visit(ProviderKeyBinding<? extends T> binding) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V visit(LinkedKeyBinding<? extends T> binding) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V visit(ExposedBinding<? extends T> binding) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V visit(UntargettedBinding<? extends T> binding) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V visit(ConstructorBinding<? extends T> binding) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V visit(ConvertedConstantBinding<? extends T> binding) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V visit(ProviderBinding<? extends T> binding) {
		// TODO Auto-generated method stub
		return null;
	}

}
