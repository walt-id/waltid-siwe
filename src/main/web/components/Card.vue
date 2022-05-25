<template>
  <div class="column">
    <div class="card">
      <header class="card-header">
        <p class="card-header-title has-text-grey">
          {{ title }}
        </p>
      </header>
      <div class="card-content">
        <div class="content has-text-centered">
          <b-icon
            :icon="icon"
            size="is-large"
            :type="{ 'is-success': iconColor === 2, 'is-info': iconColor === 1, 'is-danger': iconColor === -1, 'is-primary': iconColor === 0 }"
          />
        </div>
      </div>
      <footer class="card-footer">
        <div class="card-footer-item">
          <span>
            <slot />
          </span>
        </div>
      </footer>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BuefyCard',
  props: {
    title: {
      type: String,
      required: true
    },
    icon: {
      type: String,
      required: true
    },
    iconColor: {
      type: Number,
      required: true
    }
  }
}
</script>
