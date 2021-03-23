import logging
import re

from shell import run_shell_command

logger = logging.getLogger('Kubernetes')

chart_path = "./deployer/helm/service-chart"
output_dir = './deployer/helm/gen'


class Kubernetes:
    def __init__(self, namespace):
        self.namespace = namespace

    def create_namespace(self):
        logger.info("Creating namespace '%s'" % self.namespace)
        run_shell_command(["kubectl", "create", "namespace", self.namespace], exit_on_error=False)

    def delete_all_resources(self):
        logger.info("Deleting all resources from namespace '%s'" % self.namespace)
        run_shell_command(["kubectl", "delete", "all", "--all", "-n", self.namespace])

    def apply_resources(self, services):
        self._generate_manifests(services)
        # self._apply_manifest(service_name)

    def _generate_manifests(self, services):
        logger.info("Generating manifest...")
        cmd = [
            "helm", "template", chart_path,
            "--debug",
            "--output-dir", output_dir,
        ]
        for idx, service in enumerate(services):
            cmd += ["--set", f"services[{idx}].name={service['service']['name']}"]
            cmd += ["--set", f"services[{idx}].port={service['service']['port']}"]

        run_shell_command(cmd)

    def _apply_manifests(self):
        logger.info("Applying manifests...")
        manifest_path = f"{output_dir}/service-chart/templates"
        cmd = ["kubectl", "apply", "-f", manifest_path]
        run_shell_command(cmd)


    def format_address(self, address):
        replacements = r'\1\2.%s.svc.cluster.local:\3\4' % self.namespace
        return re.sub(r'(http\://|https\://)?(\w+)\:(\d+)(.+)', replacements, address)
