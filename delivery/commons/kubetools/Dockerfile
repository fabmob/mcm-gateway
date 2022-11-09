ARG BASE_IMAGE_UBUNTU
FROM ${BASE_IMAGE_UBUNTU}

RUN apt-get update && \
  apt-get install -y curl gettext-base jq && \
  apt install -y python python3-pip &&\
  apt install -y python3-psycopg2 &&\
  DEBIAN_FRONTEND=noninteractive TZ=Etc/UTC apt-get -y install tzdata && \
  apt install -y curl gpg gnupg2 software-properties-common apt-transport-https lsb-release ca-certificates && \
  apt install -y wget && \
  wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | apt-key add -  && \
  sh -c 'echo "deb http://apt.postgresql.org/pub/repos/apt/ `lsb_release -cs`-pgdg main" >> /etc/apt/sources.list.d/pgdg.list'  && \
  apt-get update  && \
  apt-get install -y postgresql postgresql-contrib  && \
  dpkg-reconfigure --frontend noninteractive tzdata && \
  apt install -y postgresql-13 postgresql-client-13 && \
  cd /usr/local/bin && \
  curl -s "https://raw.githubusercontent.com/kubernetes-sigs/kustomize/master/hack/install_kustomize.sh"  | bash && \
  curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl && \
  chmod +x ./kubectl && \
  curl -L https://github.com/kubernetes/kompose/releases/download/v1.26.1/kompose-linux-amd64 -o kompose && \
  chmod +x ./kompose
ADD ./scripts/* /usr/local/bin/