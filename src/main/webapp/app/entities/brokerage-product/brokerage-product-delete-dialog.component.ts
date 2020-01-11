import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBrokerageProduct } from 'app/shared/model/brokerage-product.model';
import { BrokerageProductService } from './brokerage-product.service';

@Component({
    selector: 'jhi-brokerage-product-delete-dialog',
    templateUrl: './brokerage-product-delete-dialog.component.html'
})
export class BrokerageProductDeleteDialogComponent {
    brokerageProduct: IBrokerageProduct;

    constructor(
        private brokerageProductService: BrokerageProductService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.brokerageProductService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'brokerageProductListModification',
                content: 'Deleted an brokerageProduct'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-brokerage-product-delete-popup',
    template: ''
})
export class BrokerageProductDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ brokerageProduct }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BrokerageProductDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.brokerageProduct = brokerageProduct;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
