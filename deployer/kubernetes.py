import logging
import re

from shell import run_shell_command

logger = logging.getLogger('Kubernetes')


class Kubernetes:
    def __init__(self, namespace):
        self.namespace = namespace

    def create_namespace(self):
        logger.info("Creating namespace '%s'" % self.namespace)
        run_shell_command(["kubectl", "create", "namespace", self.namespace], exit_on_error=False)

    def delete_all_resources(self):
        logger.info("Deleting all resources from namespace '%s'" % self.namespace)
        run_shell_command("kubectl delete all --all -n %s" % self.namespace)

    def format_address(self, address):
        replacements = r'\1\2.%s.svc.cluster.local:\3\4' % self.namespace
        return re.sub(r'(http\://|https\://)?(\w+)\:(\d+)(.+)', replacements, address)
