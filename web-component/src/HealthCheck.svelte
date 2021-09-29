<script>
    const appUrl = 'http://jdk17-spring-client-noahmiller-dev.apps.sandbox.x8i5.p1.openshiftapps.com'

    export let healthResult;

    $: console.log(healthResult)

    healthCheck();

    async function healthCheck() {
		try {
			const res = await fetch (appUrl + '/health')
			const {status} = await res.json();
			healthResult = status;
		} catch (error) {
			healthResult = 'UNREACHABLE'
		}
	}
</script>

{#if healthResult == 'UNSTABLE'}
	<div>Client application is currently unstable</div>
{:else if healthResult == "UNREACHABLE"}
	<div>Failed to reach client application</div>
{/if}