<!--
 This file is part of the Meeds project (https://meeds.io/).

 Copyright (C) 2020 - 2024 Meeds Association contact@meeds.io

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 3 of the License, or (at your option) any later version.
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with this program; if not, write to the Free Software Foundation,
 Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
-->
<template>
  <exo-drawer
    ref="linkedinConnectionSettingDrawer"
    v-model="drawer"
    right
    @opened="stepper = 1"
    @closed="clear">
    <template #title>
      {{ $t('linkedinConnector.admin.label.connectProfile') }}
    </template>
    <template v-if="drawer" #content>
      <v-form
        ref="ConnectionSettingForm"
        v-model="isValidForm"
        class="form-horizontal pt-0 pb-4"
        flat
        @submit="saveConnectorSetting">
        <v-stepper
          v-model="stepper"
          class="ma-0 pa-4 d-flex flex-column"
          vertical
          flat>
          <div class="flex-grow-1 flex-shrink-0">
            <v-stepper-step
              :step="1"
              class="ma-0 pa-0"
              editable>
              <span class="font-weight-bold text-color text-subtitle-1">{{ $t('linkedinConnector.admin.label.stepOne') }}</span>
            </v-stepper-step>
            <v-stepper-items>
              <v-stepper-content step="1" class="mx-0 px-0">
                <v-slide-y-transition>
                  <div class="pb-4 d-flex flex-column text-color">
                    <v-card-text class="ps-0 pb-0 text-color">
                      {{ $t('linkedinConnector.admin.label.stepOne.instructionsOne') }}
                    </v-card-text>
<!--                    <v-card-text class="ps-0 py-0 text-color">-->
<!--                      {{ $t('linkedinConnector.admin.label.stepOne.instructionsTwo') }}-->
<!--                      <a href="https://linkedin.com/settings#account" target="_blank">{{ $t('linkedinConnector.admin.label.developerSettings') }}-->
<!--                        <v-icon size="14" class="pb-1 pe-1">fas fa-external-link-alt</v-icon>-->
<!--                      </a>-->
<!--                    </v-card-text>-->
                  </div>
                </v-slide-y-transition>
              </v-stepper-content>
            </v-stepper-items>
          </div>
          <div class="flex-grow-1 flex-shrink-0">
            <v-stepper-step
              :step="2"
              class="ma-0 pa-0"
              editable>
              <span class="font-weight-bold text-color text-subtitle-1">{{ $t('linkedinConnector.admin.label.stepTwo') }}</span>
            </v-stepper-step>
            <v-stepper-items>
              <v-stepper-content step="2" class="mx-0 px-0">
                <v-slide-y-transition>
                  <div class="pb-4 d-flex flex-column text-color">
                    <v-card-text class="ps-0 py-0 text-color">
                      {{ $t('linkedinConnector.admin.label.stepTwo.instructionsOne') }}
                      <a href="https://linkedin.com/settings#oauth-apps" target="_blank">{{ $t('linkedinConnector.admin.label.oAuthApps') }} </a>
                    </v-card-text>
                    <v-card-text class="ps-0 pt-0 py-0 text-color">
                      {{ $t('linkedinConnector.admin.label.stepTwo.instructionsTwo') }}
                    </v-card-text>
                    <v-card-text class="ps-0 py-0 text-color pb-1">
                      {{ $t('linkedinConnector.admin.label.stepTwo.instructionsThree') }}
                    </v-card-text>
                    <div class="d-flex flex-row">
                      <v-text-field
                        :value="redirectUrl"
                        class="px-4 pt-0"
                        type="text"
                        outlined
                        disabled
                        dense />
                      <v-btn icon @click="copyText(redirectUrl)">
                        <v-icon>fas fa-copy</v-icon>
                      </v-btn>
                    </div>
                    <v-card-text class="ps-0 py-0 text-color pb-1">
                      {{ $t('linkedinConnector.admin.label.stepTwo.instructionsFour') }}
                    </v-card-text>
                  </div>
                </v-slide-y-transition>
              </v-stepper-content>
            </v-stepper-items>
          </div>
          <div class="flex-grow-1 flex-shrink-0">
            <v-stepper-step
              :step="3"
              class="ma-0 pa-0"
              editable>
              <span class="font-weight-bold text-color text-subtitle-1">{{ $t('linkedinConnector.admin.label.stepThree') }}</span>
            </v-stepper-step>
            <v-stepper-items>
              <v-stepper-content step="3" class="mx-0 px-0">
                <v-slide-y-transition>
                  <div class="pb-4 d-flex flex-column text-color">
                    <v-card-text class="ps-0 py-0 text-color">
                      {{ $t('linkedinConnector.admin.label.stepThree.instructionsOne') }}
                    </v-card-text>
                    <v-card-text class="ps-0 pb-4 text-color">
                      {{ $t('linkedinConnector.admin.label.stepThree.instructionsTwo') }}
                    </v-card-text>
                    <v-card-text class="text-left ps-0 py-0 text-color">
                      {{ $t('linkedinConnector.admin.label.clientId') }}
                    </v-card-text>
                    <v-card-text class="ps-0 pt-2">
                      <input
                        ref="connectorApiKey"
                        v-model="apiKey"
                        :placeholder="$t('linkedinConnector.admin.label.clientId.placeholder')"
                        type="text"
                        class="ignore-vuetify-classes full-width"
                        required
                        @input="disabled = false"
                        @change="disabled = false">
                    </v-card-text>
                    <v-card-text class="text-left ps-0 py-0 text-color">
                      {{ $t('linkedinConnector.admin.label.clientSecret') }}
                    </v-card-text>
                    <v-card-text class="ps-0 pt-2">
                      <input
                        ref="connectorSecretKey"
                        v-model="secretKey"
                        :placeholder="$t('linkedinConnector.admin.label.clientSecret.placeholder')"
                        type="text"
                        class="ignore-vuetify-classes full-width"
                        required
                        @input="disabled = false"
                        @change="disabled = false">
                    </v-card-text>
                  </div>
                </v-slide-y-transition>
              </v-stepper-content>
            </v-stepper-items>
          </div>
        </v-stepper>
      </v-form>
    </template>
    <template #footer>
      <div class="d-flex">
        <v-spacer />
        <v-btn
          v-if="stepper === 2 || stepper === 3"
          class="btn me-2"
          @click="previousStep">
          {{ $t('linkedinConnector.webhook.form.label.button.back') }}
        </v-btn>
        <v-btn
          v-else
          class="btn me-2"
          @click="close">
          {{ $t('linkedinConnector.webhook.form.label.button.cancel') }}
        </v-btn>
        <v-btn
          v-if="stepper === 1 || stepper === 2"
          class="btn btn-primary"
          @click="nextStep">
          {{ $t('linkedinConnector.webhook.form.label.button.next') }}
        </v-btn>
        <v-btn
          v-else
          :disabled="disabledSave"
          @click="saveConnectorSetting"
          class="btn btn-primary">
          {{ $t('linkedinConnector.webhook.form.label.button.save') }}
        </v-btn>
      </div>
    </template>
  </exo-drawer>
</template>

<script>
export default {
  data: () => ({
    apiKey: null,
    secretKey: null,
    isValidForm: false,
    drawer: false,
    disabled: true,
    stepper: 0,
  }),
  created() {
    this.$root.$on('linkedin-connection-setting-drawer', this.open);
  },
  computed: {
    disabledSave() {
      return this.disabled || !this.secretKey || !this.apiKey || !this.redirectUrl;
    },
    redirectUrl() {
      // return `${window.location.origin}/gamification-linkedin/rest/settings/oauthCallback`;
      return `${window.location.origin}/portal/rest/gamification/connectors/oauthCallback/linkedin`;
    }
  },
  methods: {
    open(apiKey, secretKey, redirectUrl) {
      this.apiKey = apiKey;
      this.secretKey = secretKey;
      this.redirectUrl = redirectUrl;
      if (this.$refs.linkedinConnectionSettingDrawer) {
        this.$refs.linkedinConnectionSettingDrawer.open();
      }
    },
    close() {
      if (this.$refs.linkedinConnectionSettingDrawer) {
        this.$refs.linkedinConnectionSettingDrawer.close();
      }
    },
    saveConnectorSetting() {
      this.$root.$emit('connector-settings-updated', this.apiKey, this.secretKey, this.redirectUrl);
      this.close();
    },
    clear() {
      this.apiKey = null;
      this.secretKey = null;
      this.disabled = true;
      this.stepper = 0;
    },
    previousStep() {
      this.stepper--;
      this.$forceUpdate();
    },
    nextStep(event) {
      if (event) {
        event.preventDefault();
        event.stopPropagation();
      }
      this.stepper++;
    },
    copyText(textToCopy) {
      const textArea = document.createElement('textarea');
      textArea.value = textToCopy;
      document.body.appendChild(textArea);
      textArea.select();
      document.execCommand('copy');
      document.body.removeChild(textArea);
      this.$root.$emit('alert-message', this.$t('rules.menu.linkCopied'), 'info');
    },
  }
};
</script>