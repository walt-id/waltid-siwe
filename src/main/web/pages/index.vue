<template>
  <section class="section">

    <section class="hero">
      <div class="hero-body has-text-centered">
        <p class="title">
          SIWE Demo </p>
        <p class="subtitle">
          Please login </p>

        <b-button :loading="false" :type="buttonType" class="is-large waltid" icon-left="ethereum" @click="signInOrOut">
          {{ buttonText }}
          <b-loading v-model="buttonLoading" :is-full-page="false"></b-loading>
        </b-button>
      </div>
    </section>


    <div class="columns is-mobile">
      <card :icon-color="step1" icon="wallet" title="Wallet connected">
        {{ text1 }}
      </card>

      <card :icon-color="step2" icon="login-variant" title="Signed in">
        {{ text2 }}
      </card>

      <card :icon-color="step3" icon="server-security" title="Server">
        {{ text3 }}
      </card>
    </div>
  </section>
</template>

<style scoped>
.waltid {
  color: #FFFFFF;
  font-family: 'Proxima Nova'
}
</style>

<script>
import Card from '~/components/Card'
import {ethers} from 'ethers';
import {Eip4361Message} from 'assets/Eip4361Message'


const domain = window.location.host;
const origin = window.location.origin;
let provider = null;
let signer = null;

try {
  provider = new ethers.providers.Web3Provider(window.ethereum);
  signer = provider.getSigner();
} catch (e) {
  console.log(e)
}

if (!provider || !signer) {
  alert("Please install a web3 wallet e.g. MetaMask");
  throw new Error("Stopping execution as no web3 wallet was found.");
}

export default {
  name: 'IndexPage',
  components: {
    Card
  },
  data() {
    return {
      buttonLoading: false,
      buttonText: 'Sign-in with Ethereum',
      buttonType: 'is-info',
      step1: 0,
      step2: 0,
      step3: 0,

      text1: 'Your wallet will be connected',
      text2: 'You will be signed in',
      text3: 'Sign-in with Ethereum',
      signedIn: false
    }
  },
  methods: {
    signInOrOut() {
      console.log("SignInOrOut, currently singedIn: " + this.signedIn)
      if (!this.signedIn) {
        this.signInWithEthereum()
      } else {
        this.logout()
      }
    },
    checkStep2Needed(data) {
      if (data) {
        console.log("Step 2/3: Correct type = " + (data.valid))
        console.log("Step 2/3: Valid = " + (data.valid === true))

        return !(data.valid && data.valid === true)
      } else {
        console.log("Step 2/3: Data is null")
        return false
      }
    },
    doStep1() {
      console.log("Step 1/3: Connecting to wallet...")
      return provider.send('eth_requestAccounts', []);

    },
    async doStep2() {
      console.log("Step 2/3: Signing in...")
      this.step1 = 2
      this.step2 = 1
      this.buttonText = "Signing in..."

      let loginPromise = this.login();

      if (loginPromise === false) {
        this.step2 = -1
      } else {
        console.log((await loginPromise).data)
      }
    },
    catchConnectionError(ex) {
      this.buttonLoading = false
      this.step1 = -1
      this.$buefy.dialog.alert({
        title: 'Wallet connection error',
        message: 'walt.id was not able to <i>Sign-in with Ethereum</i>: <strong>' + ex.message + '</strong>',
        type: 'is-danger',
        hasIcon: true,
        icon: 'lock-alert',
        ariaRole: 'alertdialog',
        ariaModal: true
      })
      this.buttonText = 'Wallet connection error'
    },
    catchSigningError(ex) {
      this.buttonLoading = false
      this.step2 = -1
      this.$buefy.dialog.alert({
        title: 'Sign-in error',
        message: 'walt.id was not able to <i>Sign-in with Ethereum</i>: <strong>' + ex.message + '</strong>',
        type: 'is-danger',
        hasIcon: true,
        icon: 'lock-alert',
        ariaRole: 'alertdialog',
        ariaModal: true
      })
      this.buttonText = 'Sign-in error'
    },
    async continueWithStep2(data) {
      console.log("Continue with step2: " + data)
      if (this.checkStep2Needed(data)) {
        console.log("Need to continue with step2!")
        await this.doStep2();
      }

      if (data === null) {
        console.log("Starting step2 fresh...")
        await this.doStep2();
        data = (await this.getInformation()).data
        console.log("Re-Continue with step2: " + data)
      }

      if (data.nonce === undefined) {
        console.log("Invalid data nonce?")
        await this.logout()
        return
      }

      this.text2 = "Nonce: " + data.nonce

      this.step2 = 2
      this.step3 = 2
      this.buttonText = "Successfully signed-in!"
      this.buttonLoading = false
      this.buttonType = "is-success"

      this.text3 = "Validation: " + data.valid + ", as: " + data.address
      if (data.valid) {
        this.signedIn = true
      }
    },
    async signInWithEthereum() {
      this.buttonLoading = true
      this.step1 = 1
      this.buttonText = "Connecting to wallet..."

      let step1Fail = false;
      let accounts = await this.doStep1().catch((ex) => {
        console.log("Step 1/3: Fail!")
        this.catchConnectionError(ex)
        step1Fail = true
      })
      this.text1 = `You are: ${accounts[0]}!`

      console.log("Step 1/3: Connected!")

      if (step1Fail) return

      this.step1 = 2
      this.step2 = 1
      this.buttonText = "Signing in..."

      console.log("Step 2/3: Fetching information...")

      try {
        let response = await this.getInformation()
        console.log("Got response: " + response.data)
        await this.continueWithStep2(response.data);
      } catch {
        await this.continueWithStep2(null);
      }
    },
    async login() {
      const signerAddress = await signer.getAddress();
      const description = 'Sign in with Ethereum to the app.';

      const nonce = await this.$axios.$get(this.$config.backendAddress + '/nonce', {withCredentials: true})
      console.log("Nonce: " + nonce)
      const eip4361msg = new Eip4361Message(domain, signerAddress, description, origin, '1', '1', nonce)
        .serialize()
      let signature
      try {
        signature = await signer.signMessage(eip4361msg);
      } catch (ex) {
        this.catchSigningError(ex)
        return false
      }
      console.log("Signature: " + signature)

      return this.$axios.post(this.$config.backendAddress + '/verify', {
        message: eip4361msg,
        signature: signature
      }, {withCredentials: true})
    },
    getInformation() {
      return this.$axios.get(this.$config.backendAddress + '/personal_information', {withCredentials: true});
    },
    async logout() {
      console.log("Singing out...")
      sessionStorage.clear();
      await this.$axios.post(this.$config.backendAddress  + '/logout', {}, {withCredentials: true});
      this.buttonText = 'Signed out.'
      this.buttonType = 'is-info'
      this.step1 = 0
      this.step2 = 0
      this.step3 = 0

      this.text1 = 'Your wallet will be connected'
      this.text2 = 'You will be signed in'
      this.text3 = 'Sign-in with Ethereum'
      this.signedIn = false
    }
  }
}
</script>
