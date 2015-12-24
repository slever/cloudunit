package fr.treeptik.cloudunitonitor.model.action;

import org.springframework.core.env.Environment;

import fr.treeptik.cloudunitmonitor.model.Module;
import fr.treeptik.cloudunitmonitor.utils.HipacheRedisUtils;

public class GitModuleAction extends ModuleAction {

	private static final long serialVersionUID = 1L;

	public GitModuleAction(Module parent) {
		super(parent);
	}

	@Override
	public Module enableModuleManager(HipacheRedisUtils hipacheRedisUtils,
			Module parent, Environment env, Long instanceNumber) {
		return parent;
	}

	@Override
	public void updateModuleManager(HipacheRedisUtils hipacheRedisUtils,
			Environment env) {
	}

}
