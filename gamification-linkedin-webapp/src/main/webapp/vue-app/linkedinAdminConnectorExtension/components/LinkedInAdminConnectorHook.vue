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
  <v-card
    flat
    v-on="isValidToken ? { click: openHookDetail } : {}">
    <div :class="!isValidToken && 'filter-blur-3'">
      <v-list-item class="px-0" three-line>
        <v-list-item-avatar size="58" tile>
          <v-img
            :src="avatarUrl"
            :key="avatarUrl"
            :alt="title" />
        </v-list-item-avatar>
        <v-list-item-content>
          <v-list-item-title class="align-self-start">
            {{ title }}
          </v-list-item-title>
          <v-list-item-subtitle v-if="description" class="text-truncate-2 caption mt-1 text-color">{{ description }}</v-list-item-subtitle>
          <v-list-item-subtitle class="d-flex flex-row">
            <span class="text-truncate caption d-content pt-2px text-color"> {{ watchedByLabel }} </span>
            <exo-user-avatar
              :profile-id="watchedBy"
              extra-class="ms-1"
              fullname
              popover />
          </v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-action class="d-flex flex-row align-center">
          <v-btn
            icon
            class="mx-2"
            @click="editLinkedInWebHook">
            <v-icon size="20">fas fa-edit</v-icon>
          </v-btn>
          <v-btn
            icon
            @click="deleteConfirmDialog">
            <v-icon class="error-color mx-2" size="20">fas fa-trash-alt</v-icon>
          </v-btn>
        </v-list-item-action>
      </v-list-item>
    </div>
    <v-overlay
      :value="!isValidToken"
      absolute
      opacity="0.7"
      class="d-flex position-absolute height-auto width-auto">
      <div class="d-flex flex-row">
        <div class="d-flex flex-column me-5">
          <span class="text-h6">{{ $t('linkedinConnector.webhook.linkedin.tokenExpiredOrInvalid') }}</span>
          <span class="text-h6">{{ $t('linkedinConnector.webhook.linkedin.regenerateAnotherToken') }}</span>
        </div>
        <v-btn
          class="ma-auto"
          color="primary"
          @click="editLinkedInWebHook">
          {{ $t('linkedinConnector.webhook.linkedin.reviewSettings') }}
        </v-btn>
      </div>
    </v-overlay>
    <exo-confirm-dialog
      ref="deleteHookConfirmDialog"
      :message="$t('linkedinConnector.webhook.message.confirmDeleteConnectorHook')"
      :title="$t('linkedinConnector.webhook.title.confirmDeleteProject')"
      :ok-label="$t('confirm.yes')"
      :cancel-label="$t('confirm.no')"
      @ok="deleteHook" />
  </v-card>
</template>

<script>

export default {
  props: {
    hook: {
      type: Object,
      default: null
    },
    hooksLoaded: {
      type: Boolean,
      default: false
    },
  },
  data() {
    return {
      loading: true,
      timeUntilReset: 0
    };
  },
  computed: {
    title() {
      return this.hook?.title || this.hook?.name;
    },
    name() {
      return this.hook?.name;
    },
    description() {
      return this.hook?.description;
    },
    watchedDate() {
      return this.hook?.watchedDate && new Date(this.hook.watchedDate);
    },
    avatarUrl() {
      if (this.hook?.avatarUrl) {
        return `${this.hook.avatarUrl}?version=${new Date().getTime()}`;
      } else {
        return '/gamification-linkedin/images/linkedin.png';
      }
    },
    watchedByLabel() {
      return this.$t('linkedinConnector.webhook.label.watchedBy', {0: this.$dateUtil.formatDateObjectToDisplay(this.watchedDate, {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      }, eXo.env.portal.language)});
    },
    watchedBy() {
      return this.hook?.watchedBy;
    },
    projectId() {
      return this.hook?.projectId;
    },
    isValidToken() {
      return this.hook?.tokenValid;
    }
  },
  methods: {
    deleteConfirmDialog(event) {
      if (event) {
        event.preventDefault();
        event.stopPropagation();
      }
      this.$refs.deleteHookConfirmDialog.open();
    },
    deleteHook() {
      return this.$linkedinConnectorService.deleteLinkedInWebHook(this.projectId).then(() => {
        this.$emit('hooksUpdated');
      });
    },
    editLinkedInWebHook(event) {
      if (event) {
        event.preventDefault();
        event.stopPropagation();
      }
      this.$root.$emit('linkedin-hook-form-drawer', this.hook);
    },
    openHookDetail() {
      this.$root.$emit('linkedin-hook-detail', this.hook);
    }
  }
};
</script>