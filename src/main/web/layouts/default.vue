<template>
  <div style="font-family: 'Proxima Nova';
    color: #292928;">

    <nav class="navbar" style="padding: 3px; padding-right: 10px; padding-left: 10px">

      <div class="hero" style="margin: 3px; padding: 3px; margin-left: 20px; margin-top: 15px">
        <div class="hero-body" style="margin: 3px; padding: 3px">
          <p class="title">Sign-in with Ethereum</p>
          <p class="subtitle">
            by <a href="https://walt.id">walt.id</a>
          </p>
        </div>
      </div>

      <div class="navbar-end">
        <div class="navbar-item">
          <b-button style="background-color: #0573F0; color: #FFFFFF; font-family: 'Proxima Nova'" @click="openHelp">
            Need help?
          </b-button>
        </div>
      </div>
    </nav>

    <hr style="margin: 0">

    <section class="main-content">
      <Nuxt/>
    </section>

    <div class="content has-text-centered">
      <p>siwe by <strong>walt.id</strong></p>
    </div>
  </div>
</template>

<style module>
@font-face {
  font-family: 'Proxima Nova';
  src: url("static/proxima-nova.otf");
}
</style>

<script>
const ModalForm = {template: `
<div class="modal-card" style="width: auto">
<header class="modal-card-head">
  <p class="modal-card-title">SIWE Help</p>
  <button class="delete" type="button" @click="$emit('close')"/>
</header>
<section class="modal-card-body">
  <p>You require an ethers-compatible wallet, e.g. <a href="https://metamask.io/">MetaMask</a>.</p>

  <p class="mt-4"><strong>Further readings:</strong> </p>
  <p>GitHub: <a href="https://github.com/walt-id/waltid-siwe" target="_blank">https://github.com/walt-id/waltid-siwe</a></p>
  <p>EIP-4361: <a href="https://eips.ethereum.org/EIPS/eip-4361" target="_blank">https://eips.ethereum.org/EIPS/eip-4361</a></p>
  <p>login.xyz: <a href="https://login.xyz" target="_blank">https://login.xyz</a></p>

</section>
<footer class="modal-card-foot">
  <b-button label="OK" @click="$emit('close')" type="is-primary"/>
</footer>
</div>`}

export default {
  name: 'DefaultLayout',
  methods: {
    openHelp: function () {
      this.$buefy.modal.open({
        parent: this,
        component: ModalForm,
        hasModalCard: true,
        customClass: 'custom-class custom-class-2',
        trapFocus: true
      })
    }
  },
  data() {
    return {
      items: [
        {
          title: 'Home',
          icon: 'home',
          to: {name: 'index'}
        },
        {
          title: 'Inspire',
          icon: 'lightbulb',
          to: {name: 'inspire'}
        }
      ]
    }
  }
}
</script>
