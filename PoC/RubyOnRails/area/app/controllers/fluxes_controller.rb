class FluxesController < ApplicationController
  def index
    @fluxes = Flux.all
  end
end
