<template>
  <div style="font-family: 'Proxima Nova';
    color: #292928;">

    <nav class="navbar" style="padding: 3px; padding-right: 10px; padding-left: 10px">
      <div class="navbar-brand">
        <a class="navbar-item" href="https://walt.id" style="font-family: 'Proxima Nova';
    color: #292928; font-size: calc((2 - 1) * 1.2vw + 1rem);
;">walt.id</a>
      </div>

      <div class="hero" style="margin: 3px; padding: 3px; margin-left: 20px; margin-top: 15px">
        <div class="hero-body" style="margin: 3px; padding: 3px">
          <p class="title">
            Sign-in with Ethereum</p>
          <p class="subtitle">
            by <a href="https://github.com/walt-id/waltid-ssikit-vclib">waltid-siwe</a>
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
      <p>walt-siwe by <strong>walt.id</strong></p>
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
  <p class="modal-card-title">walt.id VerifiableCredential Generator Help</p>
  <button class="delete" type="button" @click="$emit('close')"/>
</header>
<section class="modal-card-body">
  <p>You require an ethers-compatible webwallet, e.g. <strong>MetaMask</strong>.</p>

</section>
<footer class="modal-card-foot">
  <b-button label="OK" @click="$emit('close')"/>
  <b-button
    href="mailto:sales@walt.id?subject=Inquiry%20%7C%C2%A0Managed%20Cloud%20Service&body=Dear%20walt.id%20team%2C%0A%0AI%27m%20interested%20to%20learn%20more%20about%20your%20managed%20cloud%20services.%20Please%20get%20back%20to%20me%20with%20more%20information.%0A%0ABest%20regards"
    label="Further help" type="is-primary"/>
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
